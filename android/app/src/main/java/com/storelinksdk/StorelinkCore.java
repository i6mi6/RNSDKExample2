package com.storelinksdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StorelinkCore {

    // Configuration for the SDK
    public static class Configuration {
        private String refreshToken;
        private LogLevel logLevel;
        private OnConfigurationSuccessListener onConfigurationSuccess;
        private OnStoreConnectionEventListener onStoreConnectionEvent;
        private OnInvoiceEventListener onInvoiceEvent;
        private OnCheckingStoreConnectionEventListener onCheckingStoreConnectionEvent;
        private String brandName;
        private String logoUrl;
        private String devApiLocation;

        public Configuration(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public Configuration setLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Configuration setOnConfigurationSuccess(OnConfigurationSuccessListener listener) {
            this.onConfigurationSuccess = listener;
            return this;
        }

        public Configuration setOnStoreConnectionEvent(OnStoreConnectionEventListener listener) {
            this.onStoreConnectionEvent = listener;
            return this;
        }

        public Configuration setOnInvoiceEvent(OnInvoiceEventListener listener) {
            this.onInvoiceEvent = listener;
            return this;
        }

        public Configuration setOnCheckingStoreConnectionEvent(OnCheckingStoreConnectionEventListener listener) {
            this.onCheckingStoreConnectionEvent = listener;
            return this;
        }

        public Configuration setBrandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public Configuration setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Configuration setDevApiLocation(String devApiLocation) {
            this.devApiLocation = devApiLocation;
            return this;
        }
    }

    // Possible errors for the SDK
    public enum SDKError {
        INVALID_REFRESH_TOKEN,
        UNKNOWN_ERROR,
        NATIVE_COMMUNICATION_ERROR
    }

    public enum PresentationMethod {
        PRESENT_MODALLY,
        PUSH_ON_NAVIGATION_STACK,
        EMBED_IN_TAB
    }

    // Listener interfaces
    public interface OnConfigurationSuccessListener {
        void onConfigurationSuccess(Map<String, Object> data);
    }

    public interface OnStoreConnectionEventListener {
        void onStoreConnectionEvent(Map<String, Object> data);
    }

    public interface OnInvoiceEventListener {
        void onInvoiceEvent(Map<String, Object> data);
    }

    public interface OnCheckingStoreConnectionEventListener {
        void onCheckingStoreConnectionEvent(Map<String, Object> data);
    }

    public static class SDKHandler {
        private Configuration config;
        private String notificationName = "cooklist_sdk_event";
        private OnConfigurationSuccessListener onConfigurationSuccess;
        private OnStoreConnectionEventListener onStoreConnectionEvent;
        private OnInvoiceEventListener onInvoiceEvent;
        private OnCheckingStoreConnectionEventListener onCheckingStoreConnectionEvent;

        public SDKHandler(Configuration config) {
            this.config = config;
            this.onConfigurationSuccess = config.onConfigurationSuccess;
            this.onStoreConnectionEvent = config.onStoreConnectionEvent;
            this.onInvoiceEvent = config.onInvoiceEvent;
            this.onCheckingStoreConnectionEvent = config.onCheckingStoreConnectionEvent;
            setupEventListeners();
        }

        public static class SDKActivity extends StoreLinkActivity {
            // Implement as needed
        }

        public StoreLinkCoreView getBackgroundView() {
            return new StoreLinkCoreView(config.refreshToken, config.logLevel, ViewType.BACKGROUND_TASK, config.devApiLocation);
        }

        public StoreLinkCoreView getConnectUpdateStoreView(String storeId, Callback<Map<String, Object>> onComplete) {
            Map<String, Object> functionParams = new HashMap<>();
            functionParams.put("storeId", storeId);
            return new StoreLinkCoreView(config.refreshToken, config.logLevel, ViewType.CONNECT_UPDATE_STORE,
                    functionParams, onComplete, config.brandName, config.logoUrl, config.devApiLocation);
        }

        public StoreLinkCoreView getTransferCartView(String cartId, Callback<Map<String, Object>> onComplete) {
            Map<String, Object> functionParams = new HashMap<>();
            functionParams.put("cartId", cartId);
            return new StoreLinkCoreView(config.refreshToken, config.logLevel, ViewType.TRANSFER_CART,
                    functionParams, onComplete, config.brandName, config.logoUrl, config.devApiLocation);
        }

        private void setupEventListeners() {
            // Use a local broadcast receiver or EventBus to handle events
            LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(notificationName)) {
                        Map<String, Object> params = (Map<String, Object>) intent.getSerializableExtra("params");
                        String functionName = (String) params.get("functionName");
                        switch (functionName) {
                            case "onConfigurationSuccess":
                                if (onConfigurationSuccess != null) onConfigurationSuccess.call(params);
                                break;
                            case "onStoreConnectionEvent":
                                if (onStoreConnectionEvent != null) onStoreConnectionEvent.call(params);
                                break;
                            case "onInvoiceEvent":
                                if (onInvoiceEvent != null) onInvoiceEvent.call(params);
                                break;
                            case "onCheckingStoreConnectionEvent":
                                if (onCheckingStoreConnectionEvent != null) onCheckingStoreConnectionEvent.call(params);
                                break;
                        }
                    }
                }
            }, new IntentFilter(notificationName));
        }

        public void sendDataToReactNative(Map<String, Object> data) {
            Intent intent = new Intent("CooklistDataFromNative");
            intent.putExtra("data", (Serializable) data);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        // ... other methods ...
    }

    public static CompletableFuture<Result<SDKHandler, SDKError>> create(Configuration config) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SDKHandler handler = new SDKHandler(config);
                return Result.success(handler);
            } catch (Exception e) {
                if (e instanceof SDKError) {
                    return Result.failure((SDKError) e);
                } else {
                    return Result.failure(SDKError.UNKNOWN_ERROR);
                }
            }
        });
    }
}
