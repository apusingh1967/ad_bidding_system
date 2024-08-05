package com.mymodule.adbiddingsystem.common.messages;

import com.mymodule.adbiddingsystem.bidprocessors.BidRequestProcessor;

public record BidRequestWithProcessor(BidRequest bidRequest,
                                      BidRequestProcessor processor) implements Message {
}
