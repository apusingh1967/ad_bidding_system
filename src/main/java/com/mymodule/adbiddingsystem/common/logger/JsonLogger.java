package com.mymodule.adbiddingsystem.common.logger;

import com.mymodule.adbiddingsystem.common.messages.Message;
import com.mymodule.adbiddingsystem.domain.Actor;
import com.mymodule.adbiddingsystem.common.messages.LogEnd;
import com.mymodule.adbiddingsystem.common.messages.LogStart;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

@Log
public class JsonLogger extends Actor {

    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    private ConcurrentMap<String, LogStart>helperMap = new ConcurrentHashMap<>();

    @Override
    public void receive(Message message) {
        queue.add(message);
    }

    @Override @SneakyThrows
    public void run() {
        while(true){
            var message = queue.take();
            if(message instanceof LogStart start){
                helperMap.put(start.requestId(), start);
            } else if(message instanceof LogEnd end){
                var start = helperMap.get(end.requestId());
                if(start == null){
                    log.warning("start not found for: " + end);
                }
                var logObj = new Log(start.requestId(), start.bid(),
                        end.wonBid(), end.end() - start.start());
                var om = new ObjectMapper();
                var logJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(logObj);
                log.info(logJson); // output where? to some data lake maybe
            }
        }
    }

    public void shutdown(){

    }
}
