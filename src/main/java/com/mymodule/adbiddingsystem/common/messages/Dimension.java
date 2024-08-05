package com.mymodule.adbiddingsystem.common.messages;

public record Dimension(int length, int height) {

    public boolean contains(Dimension other) {
        return other.length <= this.length && other.height <= this.height;
    }
}
