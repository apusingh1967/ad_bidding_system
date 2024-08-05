package com.adbiddingsystem.domain.dsp;

import com.mymodule.adbiddingsystem.common.messages.BidRequest;
import com.mymodule.adbiddingsystem.common.messages.Dimension;
import com.mymodule.adbiddingsystem.domain.dsp.Campaign;
import com.mymodule.adbiddingsystem.domain.dsp.SimpleBidProcessingStrategy;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;
public class SimpleBidProcessingStrategyTest {

    private SimpleBidProcessingStrategy strategy = new SimpleBidProcessingStrategy();

    @Test
    public void whenOneMatches() {
        var bidRequest = new BidRequest("br1", "apple.com", "CA",
                new Dimension(55, 55));
        var campaigns = List.of(
                new Campaign("cid1", "CA", "apple.com",
                        List.of(new Dimension(55, 55), new Dimension(55, 56))));
        var bid = strategy.processBidRequest(bidRequest, campaigns, null /* dont care in this test */);
        assertTrue(bid.isPresent());
        assertTrue(bid.get().campaign().dimensions().size() == 1);
    }

    @Test
    public void whenTwoMatches() {
        var bidRequest = new BidRequest("br1", "apple.com", "CA",
                new Dimension(55, 55));
        var campaigns = List.of(
                new Campaign("cid1", "CA", "apple.com",
                        List.of(new Dimension(55, 55), new Dimension(55, 55))));
        var bid = strategy.processBidRequest(bidRequest, campaigns, null /* dont care in this test */);
        assertTrue(bid.isPresent());
        assertTrue(bid.get().campaign().dimensions().size() == 2);
    }

    @Test
    public void whenNoneMatches() {
        var bidRequest = new BidRequest("br1", "apple.com", "CA",
                new Dimension(55, 55));
        var campaigns = List.of(
                new Campaign("cid1", "CA", "apple.com",
                        List.of(new Dimension(56, 55), new Dimension(55, 56))));
        var bid = strategy.processBidRequest(bidRequest, campaigns, null /* dont care in this test */);
        assertTrue(bid.isEmpty());
    }

    @Test
    public void when70PercentDomainMatches() {
        var bidRequest = new BidRequest("br1", "http://www.apple.ca", "CA",
                new Dimension(55, 55));
        var campaigns = List.of(
                new Campaign("cid1", "CA", "apple.com",
                        List.of(new Dimension(55, 55), new Dimension(55, 56))));
        var bid = strategy.processBidRequest(bidRequest, campaigns, null /* dont care in this test */);
        assertTrue(bid.isPresent());
    }

}
