package com.storelink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import java.util.HashMap;
import java.util.Map;

import androidx.core.util.Consumer;
import androidx.lifecycle.Observer;

import android.os.Looper;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class SDKHandler {
    private Context context;
    private String refreshToken;
    private String brandName;
    private String logoUrl;
    private String _devApiLocation;
    private LogLevel _logLevel;
    private Consumer<HashMap<String, Object>> onConfigurationSuccess;
    private Consumer<HashMap<String, Object>> onStoreConnectionEvent;
    private Consumer<HashMap<String, Object>> onInvoiceEvent;
    private Consumer<HashMap<String, Object>> onCheckingStoreConnectionEvent;
    private boolean isConfigurationSuccessCalled = false;
    
    private static final String EVENT_NAME = "CooklistDataFromNative";

    public SDKHandler(Context context, String refreshToken, String brandName, String logoUrl) {
        this(context, refreshToken, brandName, logoUrl, null, null);
    }

    public SDKHandler(Context context, String refreshToken, String brandName, String logoUrl, String _devApiLocation) {
        this(context, refreshToken, brandName, logoUrl, _devApiLocation, null);
    }

    public SDKHandler(Context context, String refreshToken, String brandName, String logoUrl, LogLevel _logLevel) {
        this(context, refreshToken, brandName, logoUrl, null, _logLevel);
    }

    public SDKHandler(Context context, String refreshToken, String brandName, String logoUrl, String _devApiLocation, LogLevel _logLevel) {
        this.context = context;
        this.refreshToken = refreshToken;
        this.brandName = brandName;
        this.logoUrl = logoUrl;
        this._devApiLocation = _devApiLocation;
        this._logLevel = _logLevel;
        this.setupNotificationObservers();
    }

    public void setDevApiLocation(String _devApiLocation) {
        this._devApiLocation = _devApiLocation;
    }

    public void setLogLevel(LogLevel _logLevel) {
        this._logLevel = _logLevel;
    }

    public void setOnConfigurationSuccess(Consumer<HashMap<String, Object>> onConfigurationSuccess) {
        this.onConfigurationSuccess = onConfigurationSuccess;
    }

    public void setOnStoreConnectionEvent(Consumer<HashMap<String, Object>> onStoreConnectionEvent) {
        this.onStoreConnectionEvent = onStoreConnectionEvent;
    }

    public void setOnInvoiceEvent(Consumer<HashMap<String, Object>> onInvoiceEvent) {
        this.onInvoiceEvent = onInvoiceEvent;
    }

    public void setOnCheckingStoreConnectionEvent(Consumer<HashMap<String, Object>> onCheckingStoreConnectionEvent) {
        this.onCheckingStoreConnectionEvent = onCheckingStoreConnectionEvent;
    }

    private void setupNotificationObservers() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            EventManager.getInstance().getEventLiveData().observeForever(new Observer<Map<String, Object>>() {
                @Override
                public void onChanged(Map<String, Object> event) {
                    if (event != null) {
                        String functionName = (String) event.get("functionName");
                        if (functionName != null) {
                            switch (functionName) {
                                case "onConfigurationSuccess":
                                    if (onConfigurationSuccess != null && !isConfigurationSuccessCalled) {
                                        onConfigurationSuccess.accept((HashMap<String, Object>) event);
                                        isConfigurationSuccessCalled = true; // Set the flag to true after the first call
                                    }
                                    break;
                                case "onStoreConnectionEvent":
                                    if (onStoreConnectionEvent != null) {
                                        onStoreConnectionEvent.accept((HashMap<String, Object>) event);
                                    }
                                    break;
                                case "onInvoiceEvent":
                                    if (onInvoiceEvent != null) {
                                        onInvoiceEvent.accept((HashMap<String, Object>) event);
                                    }
                                    break;
                                case "onCheckingStoreConnectionEvent":
                                    if (onCheckingStoreConnectionEvent != null) {
                                        onCheckingStoreConnectionEvent.accept((HashMap<String, Object>) event);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            });
        });
    }

    private Bundle getCommonBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("refreshToken", refreshToken);
        if (_logLevel != null) {
            bundle.putInt("logLevel", _logLevel.getIntValue());
        }
        bundle.putString("brandName", brandName);
        bundle.putString("logoUrl", logoUrl);
        if (_devApiLocation != null) {
            bundle.putString("devApiLocation", _devApiLocation);
        }
        return bundle;
    }

    public Fragment getBackgroundView() {
        Bundle args = getCommonBundle();
        args.putString("viewType", ViewType.BACKGROUND_TASK.getStringValue());

        StorelinkFragment fragment = new StorelinkFragment();
        fragment.setArguments(args);
//        fragment.setCallbacks(onConfigurationSuccess, onStoreConnectionEvent, onInvoiceEvent, onCheckingStoreConnectionEvent);
        return fragment;
    }

    public Intent getConnectUpdateStoreView(String storeId) {
        Intent intent = new Intent(context, ConnectOrUpdateStoreActivity.class);

        intent.putExtras(getCommonBundle());
        intent.putExtra("viewType", ViewType.CONNECT_UPDATE_STORE.getStringValue());

        HashMap<String, String> functionParams = new HashMap<>();
        functionParams.put("storeId", storeId);
        intent.putExtra("functionParams", functionParams);

        return intent;
    }
}
