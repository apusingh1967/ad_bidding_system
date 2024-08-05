package com.mymodule.adbiddingsystem.common.messages;

import com.mymodule.adbiddingsystem.domain.dsp.DspActor;
import com.mymodule.adbiddingsystem.domain.dsp.Campaign;

import java.math.BigDecimal;

public record Bid(String requestId, BigDecimal quote, Campaign campaign, BidRequest bidRequest, DspActor dsp) implements Message {
}
