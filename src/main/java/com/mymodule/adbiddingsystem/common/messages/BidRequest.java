package com.mymodule.adbiddingsystem.common.messages;

public record BidRequest(String requestId, String url, String country, Dimension dimension)
        implements Message {
}
