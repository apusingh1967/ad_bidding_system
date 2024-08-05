package com.mymodule.adbiddingsystem.domain.dsp;

import com.mymodule.adbiddingsystem.common.messages.Bid;
import com.mymodule.adbiddingsystem.common.messages.BidRequest;

import java.util.List;
import java.util.Optional;

public interface BidProcessingStrategy {

    Optional<Bid> processBidRequest(BidRequest bidRequest, List<Campaign>campaigns, DspActor dsp);
}
