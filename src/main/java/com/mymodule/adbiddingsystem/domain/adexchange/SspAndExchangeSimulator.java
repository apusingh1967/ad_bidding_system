package com.mymodule.adbiddingsystem.domain.adexchange;

import com.mymodule.adbiddingsystem.Main;
import com.mymodule.adbiddingsystem.bidprocessors.BidRequestProcessor;
import com.mymodule.adbiddingsystem.common.logger.JsonLogger;
import com.mymodule.adbiddingsystem.common.messages.*;
import com.mymodule.adbiddingsystem.domain.Actor;
import com.mymodule.adbiddingsystem.domain.dsp.DspActor;
import com.adbiddingsystem.common.messages.*;
import com.mymodules.com.adbiddingsystem.common.messages.*;
import lombok.SneakyThrows;
import lombok.extern.java.Log;


import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
@Log
public class SspAndExchangeSimulator extends Actor {
    private BlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private List<DspActor> dspActors = new CopyOnWriteArrayList<>();
    private JsonLogger logger;
    private Comparator<Bid> bidResolveStrategy;
    private Main caller;
    private ExecutorService bidRequestProcessorThreadPool = Executors.newCachedThreadPool();
    public SspAndExchangeSimulator(JsonLogger logger, Comparator<Bid> bidResolveStrategy, Main caller){
        this.logger = logger;
        this.bidResolveStrategy = bidResolveStrategy;
        this.caller = caller;
    }
    @Override @SneakyThrows
    public void run() {
        while(true) {
            var message = messages.take();
            if(message instanceof BidRequest bidRequest) {
                logger.receive(new LogStart(((BidRequest) message).requestId(), System.currentTimeMillis(),
                        dspActors.size(), bidRequest));
                var processor = new BidRequestProcessor(bidResolveStrategy);
                var bidRequestWithProcessor = new BidRequestWithProcessor(bidRequest, processor);
                dspActors.forEach(dsp -> dsp.receive(bidRequestWithProcessor));
                bidRequestProcessorThreadPool.submit(processor);
            } else if(message instanceof BidResponse bidResponse){
                logger.receive(new LogEnd(((BidResponse) message).requestId(), System.currentTimeMillis(),
                        bidResponse.campaigns(), bidResponse.campaign(), bidResponse));
                caller.countdown();
            } else {
                log.severe("not expected");
            }
        }
    }

    // observer pattern at work
    public void register(DspActor dspActor) {
        dspActors.add(dspActor);
    }

    @Override
    public void receive(Message message){
        boolean added = messages.add(message);
    }

    public void shutdown(){
        bidRequestProcessorThreadPool.shutdown();
    }
}
