package com.storelink;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

import java.util.HashMap;
import java.util.Map;

public class StorelinkActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    private ReactNativeViewManager mReactNativeViewManager;
    private ReactRootView mReactRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String refreshToken = intent.getStringExtra("refreshToken");
        ViewType viewType = ViewType.valueOf(intent.getStringExtra("viewType"));
        LogLevel logLevel = LogLevel.valueOf(intent.getStringExtra("logLevel"));
        String brandName = intent.getStringExtra("brandName");
        String logoUrl = intent.getStringExtra("logoUrl");
        String devApiLocation = intent.getStringExtra("devApiLocation");
        HashMap<String, String> functionParams = (HashMap<String, String>) intent.getSerializableExtra("functionParams");

        mReactNativeViewManager = new ReactNativeViewManager(getApplication(), this);
        mReactRootView = mReactNativeViewManager.createReactNativeView(this, refreshToken, viewType, logLevel, functionParams, brandName, logoUrl, devApiLocation);
        setContentView(mReactRootView);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReactNativeViewManager.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReactNativeViewManager.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReactNativeViewManager.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        mReactNativeViewManager.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mReactNativeViewManager.onKeyUp(keyCode)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
