package com.storelinksdk;

public enum LogLevel {
    NONE,
    DEBUG,
    DEV;

    public int getIntValue() {
        switch (this) {
            case NONE:
                return 1;
            case DEBUG:
                return 2;
            case DEV:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown LogLevel");
        }
    }
}