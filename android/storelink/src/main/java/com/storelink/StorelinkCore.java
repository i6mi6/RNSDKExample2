package com.storelink;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.Map;

public class StorelinkCore {

    // Configuration for the SDK
    public static class Configuration {
        private String refreshToken;
        private LogLevel logLevel;
        private OnEventListener onConfigurationSuccess;
        private OnEventListener onStoreConnectionEvent;
        private OnEventListener onInvoiceEvent;
        private OnEventListener onCheckingStoreConnectionEvent;
        private String brandName;
        private String logoUrl;
        private String devApiLocation;

        public Configuration(String refreshToken, LogLevel logLevel, OnEventListener onConfigurationSuccess, OnEventListener onStoreConnectionEvent, OnEventListener onInvoiceEvent, OnEventListener onCheckingStoreConnectionEvent, String brandName, String logoUrl, String devApiLocation) {
            this.refreshToken = refreshToken;
            this.logLevel = logLevel;
            this.onConfigurationSuccess = onConfigurationSuccess;
            this.onStoreConnectionEvent = onStoreConnectionEvent;
            this.onInvoiceEvent = onInvoiceEvent;
            this.onCheckingStoreConnectionEvent = onCheckingStoreConnectionEvent;
            this.brandName = brandName;
            this.logoUrl = logoUrl;
            this.devApiLocation = devApiLocation;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public LogLevel getLogLevel() {
            return logLevel;
        }

        public OnEventListener getOnConfigurationSuccess() {
            return onConfigurationSuccess;
        }

        public OnEventListener getOnStoreConnectionEvent() {
            return onStoreConnectionEvent;
        }

        public OnEventListener getOnInvoiceEvent() {
            return onInvoiceEvent;
        }

        public OnEventListener getOnCheckingStoreConnectionEvent() {
            return onCheckingStoreConnectionEvent;
        }

        public String getBrandName() {
            return brandName;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public String getDevApiLocation() {
            return devApiLocation;
        }
    }

    // Possible errors for the SDK
    public enum SDKError {
        INVALID_REFRESH_TOKEN,
        UNKNOWN_ERROR,
        NATIVE_COMMUNICATION_ERROR
    }

    // Define a handler that the SDK uses
    public static class SDKHandler {

        private Configuration config;

        public SDKHandler(Configuration config) {
            this.config = config;
            setupNotificationObservers();
        }

        private void setupNotificationObservers() {
            EventManager.getInstance().getEventLiveData().observeForever(event -> {
                if (event != null && event.containsKey("functionName")) {
                    String functionName = (String) event.get("functionName");
                    switch (functionName) {
                        case "onConfigurationSuccess":
                            if (config.getOnConfigurationSuccess() != null) {
                                config.getOnConfigurationSuccess().onEvent(event);
                            }
                            break;
                        case "onStoreConnectionEvent":
                            if (config.getOnStoreConnectionEvent() != null) {
                                config.getOnStoreConnectionEvent().onEvent(event);
                            }
                            break;
                        case "onInvoiceEvent":
                            if (config.getOnInvoiceEvent() != null) {
                                config.getOnInvoiceEvent().onEvent(event);
                            }
                            break;
                        case "onCheckingStoreConnectionEvent":
                            if (config.getOnCheckingStoreConnectionEvent() != null) {
                                config.getOnCheckingStoreConnectionEvent().onEvent(event);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        public Fragment getBackgroundView() {
            return StorelinkCoreViewFragment.newInstance(config.getRefreshToken(), config.getLogLevel(), ViewType.BACKGROUND_TASK, null);
        }

        public Fragment getConnectUpdateStoreView(String storeId, OnCompleteListener onComplete) {
            return StorelinkCoreViewFragment.newInstance(config.getRefreshToken(), config.getLogLevel(), ViewType.CONNECT_UPDATE_STORE, Map.of("storeId", storeId), onComplete);
        }

        public Fragment getTransferCartView(String cartId, OnCompleteListener onComplete) {
            return StorelinkCoreViewFragment.newInstance(config.getRefreshToken(), config.getLogLevel(), ViewType.TRANSFER_CART, Map.of("cartId", cartId), onComplete);
        }

        public void sendDataToReactNative(Map<String, Object> data) {
            EventManager.getInstance().postEvent(data);
        }
    }

    // Create a handler based on configuration
    public static SDKHandler create(Configuration config) {
        return new SDKHandler(config);
    }
}
