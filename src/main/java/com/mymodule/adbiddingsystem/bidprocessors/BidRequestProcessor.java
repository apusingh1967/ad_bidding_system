package com.mymodule.adbiddingsystem.bidprocessors;

import com.mymodule.adbiddingsystem.common.messages.Bid;
import com.mymodule.adbiddingsystem.common.messages.BidWon;
import com.mymodule.adbiddingsystem.common.config.ConfigObject;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Object shared between Exchange and DSP
 * It is short lived, and spans the life of processing one bid only
 * It uses a simply max bid strategy to resolve bid responses coming from DSP
 * After a timeout, it processes and returns highest bid to Exchange
 */
@Log
public class BidRequestProcessor implements Runnable {
    private ConcurrentSkipListSet<Bid> bids;
    public BidRequestProcessor(Comparator<Bid>strategy){
        bids = new ConcurrentSkipListSet<>(strategy);
    }
    public void bid(Bid bid){
        bids.add(bid);
    }
    @Override @SneakyThrows
    public void run() {
        // wait fixed time for bids
        Thread.sleep(ConfigObject.instance.WINDOW_TO_SUBMIT_BIDS_IN_MILLIS);
        if(bids.size() > 0) {
            var bidWon = bids.first();
            bidWon.dsp().receive(new BidWon(bidWon.requestId(), bidWon.campaign(), bidWon.quote()));
        } else {
            log.info("Bid was not called");
        }
    }
}
