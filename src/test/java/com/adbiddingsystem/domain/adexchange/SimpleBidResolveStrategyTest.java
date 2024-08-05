package com.adbiddingsystem.domain.adexchange;

import com.mymodule.adbiddingsystem.common.messages.Bid;
import com.mymodule.adbiddingsystem.domain.adexchange.SimpleBidResolveStrategy;
import org.junit.Test;

import java.math.BigDecimal;
import static org.junit.Assert.*;

public class SimpleBidResolveStrategyTest {

    private SimpleBidResolveStrategy strategy = new SimpleBidResolveStrategy();

    @Test
    public void whenOneGreaterThanOther() {
        var bid1 = new Bid("id1", new BigDecimal("1.11"), null, null, null);
        var bid2 = new Bid("id2", new BigDecimal("1.09"), null, null, null);
        var i = strategy.compare(bid1, bid2);
        assertTrue(i > 0);
    }

    @Test
    public void whenOneLessThanOther() {
        var bid1 = new Bid("id1", new BigDecimal("1.09"), null, null, null);
        var bid2 = new Bid("id2", new BigDecimal("1.11"), null, null, null);
        var i = strategy.compare(bid1, bid2);
        assertTrue(i < 0);
    }

    @Test
    public void whenBothEqual() {
        var bid1 = new Bid("id1", new BigDecimal("1.11"), null, null, null);
        var bid2 = new Bid("id2", new BigDecimal("1.11"), null, null, null);
        var i = strategy.compare(bid1, bid2);
        assertTrue(i == 0);
    }

    @Test(expected = NullPointerException.class)
    public void whenOneNull() {
        var bid1 = new Bid("id1", new BigDecimal("1.11"), null, null, null);
        strategy.compare(bid1, null);
    }
}
