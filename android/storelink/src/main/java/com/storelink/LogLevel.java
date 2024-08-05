package com.storelink;

public enum LogLevel {
    NONE(1),
    DEBUG(2),
    DEV(3);

    private final int intValue;

    LogLevel(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
