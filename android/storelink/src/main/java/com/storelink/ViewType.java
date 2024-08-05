package com.storelink;

public enum ViewType {
    BACKGROUND_TASK("BACKGROUND_TASK"),
    STORE_CONNECTIONS_LIST("STORE_CONNECTIONS_LIST"),
    CONNECT_UPDATE_STORE("CONNECT_UPDATE_STORE"),
    TRANSFER_CART("TRANSFER_CART"),
    DEVICE_UUID("DEVICE_UUID");

    private final String stringValue;

    ViewType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
