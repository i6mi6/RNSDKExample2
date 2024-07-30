package com.storelink;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

public class StorelinkCore {

    public static class Configuration {
        String refreshToken;
        LogLevel logLevel;
        Callback onConfigurationSuccess;
        Callback onStoreConnectionEvent;
        Callback onInvoiceEvent;
        Callback onCheckingStoreConnectionEvent;
        String brandName;
        String logoUrl;
        String _devApiLocation;

        public Configuration(String refreshToken, LogLevel logLevel, Callback onConfigurationSuccess, Callback onStoreConnectionEvent, Callback onInvoiceEvent, Callback onCheckingStoreConnectionEvent, String brandName, String logoUrl, String _devApiLocation) {
            this.refreshToken = refreshToken;
            this.logLevel = logLevel;
            this.onConfigurationSuccess = onConfigurationSuccess;
            this.onStoreConnectionEvent = onStoreConnectionEvent;
            this.onInvoiceEvent = onInvoiceEvent;
            this.onCheckingStoreConnectionEvent = onCheckingStoreConnectionEvent;
            this.brandName = brandName;
            this.logoUrl = logoUrl;
            this._devApiLocation = _devApiLocation;
        }
    }

    public static class SDKHandler {
        private Configuration config;

        public SDKHandler(Configuration config) {
            this.config = config;
        }

        public Fragment getBackgroundView(Context context) {
            return StorelinkCoreViewFragment.newInstance(
                    config.refreshToken,
                    ViewType.BACKGROUND_TASK,
                    config.logLevel,
                    null,
                    config.brandName,
                    config.logoUrl,
                    config._devApiLocation
            );
        }

        public Fragment getConnectUpdateStoreView(Context context, String storeId, Callback onComplete) {
            Bundle functionParams = new Bundle();
            functionParams.putString("storeId", storeId);
            return StorelinkCoreViewFragment.newInstance(
                    config.refreshToken,
                    ViewType.CONNECT_UPDATE_STORE,
                    config.logLevel,
                    functionParams,
                    config.brandName,
                    config.logoUrl,
                    config._devApiLocation
            );
        }

        public Fragment getTransferCartView(Context context, String cartId, Callback onComplete) {
            Bundle functionParams = new Bundle();
            functionParams.putString("cartId", cartId);
            return StorelinkCoreViewFragment.newInstance(
                    config.refreshToken,
                    ViewType.TRANSFER_CART,
                    config.logLevel,
                    functionParams,
                    config.brandName,
                    config.logoUrl,
                    config._devApiLocation
            );
        }
    }

    public static SDKHandler create(Configuration config) {
        return new SDKHandler(config);
    }

    public interface Callback {
        void onEvent(Map<String, Object> params);
    }

    public enum LogLevel {
        NONE,
        DEBUG,
        DEV;
    }

    public enum ViewType {
        BACKGROUND_TASK,
        STORE_CONNECTIONS_LIST,
        CONNECT_UPDATE_STORE,
        TRANSFER_CART,
        DEVICE_UUID;
    }
}
