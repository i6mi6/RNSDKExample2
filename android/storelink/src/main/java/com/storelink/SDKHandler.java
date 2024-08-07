package com.storelink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import androidx.core.util.Consumer;

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
