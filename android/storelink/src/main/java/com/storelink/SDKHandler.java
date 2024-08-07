package com.storelink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class SDKHandler {
    private Context context;
    private String refreshToken;
    private String brandName;
    private String logoUrl;
    private String devApiLocation;
    private String logLevel;

    public SDKHandler(Context context, String refreshToken, String brandName, String logoUrl, String devApiLocation, String logLevel) {
        this.context = context;
        this.refreshToken = refreshToken;
        this.brandName = brandName;
        this.logoUrl = logoUrl;
        this.devApiLocation = devApiLocation;
        this.logLevel = logLevel;
    }

    private Bundle getCommonBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("refreshToken", refreshToken);
        bundle.putString("logLevel", logLevel);
        bundle.putString("brandName", brandName);
        bundle.putString("logoUrl", logoUrl);
        bundle.putString("devApiLocation", devApiLocation);
        return bundle;
    }

    public Fragment getBackgroundView() {
        Bundle args = getCommonBundle();
        args.putString("viewType", ViewType.BACKGROUND_TASK.getStringValue());
        
        StorelinkFragment fragment = new StorelinkFragment();
        fragment.setArguments(args);
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