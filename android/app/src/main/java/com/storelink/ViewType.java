package com.storelink;

public enum ViewType {
    BACKGROUND_TASK,
    STORE_CONNECTIONS_LIST,
    CONNECT_UPDATE_STORE,
    TRANSFER_CART,
    DEVICE_UUID;

    public String getStringValue() {
        switch (this) {
            case BACKGROUND_TASK:
                return "BACKGROUND_TASK";
            case STORE_CONNECTIONS_LIST:
                return "STORE_CONNECTIONS_LIST";
            case CONNECT_UPDATE_STORE:
                return "CONNECT_UPDATE_STORE";
            case TRANSFER_CART:
                return "TRANSFER_CART";
            case DEVICE_UUID:
                return "DEVICE_UUID";
            default:
                throw new IllegalArgumentException("Unknown ViewType");
        }
    }
}