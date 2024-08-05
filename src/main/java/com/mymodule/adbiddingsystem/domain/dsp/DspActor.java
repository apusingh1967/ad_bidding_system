package com.mymodule.adbiddingsystem.domain.dsp;

import com.adbiddingsystem.common.messages.*;
import com.mymodule.adbiddingsystem.common.messages.BidResponse;
import com.mymodule.adbiddingsystem.common.messages.BidRequestWithProcessor;
import com.mymodule.adbiddingsystem.common.messages.BidWon;
import com.mymodule.adbiddingsystem.common.messages.Message;
import com.mymodule.adbiddingsystem.domain.adexchange.SspAndExchangeSimulator;
import com.mymodule.adbiddingsystem.domain.Actor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

@Log
public class DspActor extends Actor {

    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    private List<Campaign>campaigns = new CopyOnWriteArrayList<>();
    private BidProcessingStrategy strategy;
    private SspAndExchangeSimulator sspAndExchangeSimulator;

    public DspActor(SspAndExchangeSimulator sspAndExchangeSimulator, BidProcessingStrategy strategy){
        this.sspAndExchangeSimulator = sspAndExchangeSimulator;
        this.strategy = strategy;
        sspAndExchangeSimulator.register(this);
    }
    @Override
    public void receive(Message message) {
        boolean added = queue.add(message);
    }

    @Override @SneakyThrows
    public void run() {
        while(true) {
            var message = queue.take();
            if(message instanceof BidRequestWithProcessor bidRequestWithProcessor) {
                var bid = strategy.processBidRequest(bidRequestWithProcessor.bidRequest(), campaigns, this);
                if(bid.isPresent()) bidRequestWithProcessor.processor().bid(bid.get());
            } else if(message instanceof BidWon bidWon) {
               var bidResponse = new BidResponse(bidWon.requestId(), bidWon.campaign(), campaigns, bidWon.qoute());
               sspAndExchangeSimulator.receive(bidResponse);
            }
        }
    }

    public void addCampaign(Campaign campaign){
        campaigns.add(campaign);
    }

    public boolean removeCampaign(Campaign campaign) {
        return campaigns.remove(campaign);
    }
}
