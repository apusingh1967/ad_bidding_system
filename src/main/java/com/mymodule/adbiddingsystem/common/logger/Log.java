package com.mymodule.adbiddingsystem.common.logger;

import com.mymodule.adbiddingsystem.common.messages.BidRequest;
import com.mymodule.adbiddingsystem.common.messages.BidResponse;

public record Log(String requestId, BidRequest bidRequest, BidResponse wonBid, long evaluationTime) {
}
