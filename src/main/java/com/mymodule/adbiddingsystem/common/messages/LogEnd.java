package com.mymodule.adbiddingsystem.common.messages;

import com.mymodule.adbiddingsystem.domain.dsp.Campaign;

import java.util.List;

public record LogEnd(String requestId, long end, List<Campaign>campaigns, Campaign wonCampaign,
                     BidResponse wonBid)
        implements Message{
}
