package com.mymodule.adbiddingsystem;

import com.mymodule.adbiddingsystem.common.logger.JsonLogger;
import com.mymodule.adbiddingsystem.common.messages.BidRequest;
import com.mymodule.adbiddingsystem.common.messages.Dimension;
import com.mymodule.adbiddingsystem.domain.adexchange.SimpleBidResolveStrategy;
import com.mymodule.adbiddingsystem.domain.adexchange.SspAndExchangeSimulator;
import com.mymodule.adbiddingsystem.domain.dsp.Campaign;
import com.mymodule.adbiddingsystem.domain.dsp.DspActor;
import com.mymodule.adbiddingsystem.domain.dsp.SimpleBidProcessingStrategy;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This is a driver kind of which will run though actors and print a json output
 * There is one bid which succeeds and another one which does not
 * Also, see test cases
 */
@Log
public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private CountDownLatch latch;

    @SneakyThrows
    private void run() {
        var logger = new JsonLogger();
        var lt = new Thread(logger);
        lt.setDaemon(true);
        lt.start();
        var driver = new SspAndExchangeSimulator(logger, new SimpleBidResolveStrategy(), this);
        var d = new Thread(driver);
        d.setDaemon(true);
        d.start();


        latch = new CountDownLatch(1); // waiting for successful bids only.. only one is expected

        // one exchange --> two DSPs
        // Two bids submitted
        // One of them matches rules defined in SimpleBidProcessingStrategy
        var dsp1 = new DspActor(driver, new SimpleBidProcessingStrategy());
        dsp1.addCampaign(new Campaign("cid1", "CA", "apple.com",
                List.of(new Dimension(99, 150), new Dimension(200, 100))));
        dsp1.addCampaign(new Campaign("cid2", "US", "apple.com",
                List.of(new Dimension(100, 100), new Dimension(100, 200))));


        var dsp2 = new DspActor(driver, new SimpleBidProcessingStrategy());
        dsp2.addCampaign(new Campaign("cid1", "CA", "apple.com",
                List.of(new Dimension(100, 100 /* will not produce bid */), new Dimension(200, 100))));
        dsp2.addCampaign(new Campaign("cid2", "US", "apple.com",
                List.of(new Dimension(50, 51 /* will not produce bid*/), new Dimension(100, 200))));
        var t2 = new Thread(dsp2);
        t2.setDaemon(true);
        t2.start();

        // here is bid request for which countdown latch will await
        driver.receive(new BidRequest("br1", "a.apple.ca", "CA",
                new Dimension(100, 50)));
        // another bid
        driver.receive(new BidRequest("br1", "a.apple.ca", "CA",
                new Dimension(100, 150)));

        latch.await();
        driver.shutdown();
        lt.join();
        System.out.println("exiting...");
//        Thread.sleep(1000); // give time for log to be printed
//        System.exit(0);
    }

    public void countdown(){
        latch.countDown();
    }

}
