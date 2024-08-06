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
    private ReactNativeFragmentManager mReactNativeFragmentManager;
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

//        mReactNativeFragmentManager = new ReactNativeFragmentManager(getApplication(), this);
//        mReactRootView = mReactNativeFragmentManager.createReactNativeView(this, refreshToken, viewType, logLevel, functionParams, brandName, logoUrl, devApiLocation);
//        setContentView(mReactRootView);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReactNativeFragmentManager.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReactNativeFragmentManager.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReactNativeFragmentManager.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        mReactNativeFragmentManager.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mReactNativeFragmentManager.onKeyUp(keyCode)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
