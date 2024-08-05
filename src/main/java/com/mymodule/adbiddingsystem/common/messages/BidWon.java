package com.mymodule.adbiddingsystem.common.messages;

import com.mymodule.adbiddingsystem.domain.dsp.Campaign;

import java.math.BigDecimal;

public record BidWon(String requestId, Campaign campaign, BigDecimal qoute) implements Message {
}
