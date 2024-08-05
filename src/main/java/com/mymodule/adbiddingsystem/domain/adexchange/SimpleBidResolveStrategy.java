package com.mymodule.adbiddingsystem.domain.adexchange;

import com.mymodule.adbiddingsystem.common.messages.Bid;

import java.util.Comparator;
import java.util.Objects;
public class SimpleBidResolveStrategy implements Comparator<Bid> {
    @Override
    public int compare(Bid o1, Bid o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);
        return o1.quote().compareTo(o2.quote());
    }

}
