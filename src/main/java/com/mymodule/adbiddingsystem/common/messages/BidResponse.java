package com.mymodule.adbiddingsystem.common.messages;

import com.mymodule.adbiddingsystem.domain.dsp.Campaign;

import java.math.BigDecimal;
import java.util.List;

public record BidResponse(String requestId, Campaign campaign, List<Campaign>campaigns, BigDecimal quote)
        implements Message {
}
