package com.mymodule.adbiddingsystem.common.messages;

public record LogStart(String requestId, long start, int noOfBids, BidRequest bid) implements Message{
}
