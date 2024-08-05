package com.storelink;

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

        mReactNativeViewManager = new ReactNativeViewManager(getApplication(), this);

        // Assuming these variables are set somewhere in your activity
        String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo5ODkxMzksIm9pZCI6MiwidmVyc2lvbiI6MSwianRpIjoiZjRkNGQzYTEtNzlkOC00MWQ3LTliMDYtZTk2MGU0MDFkODJmIiwiaXNzdWVkX2F0IjoxNzIyODk2NzE1LjIwMjg5OSwiZXhwaXJlc19hdCI6MTc1NDQzMjcxNS4yMDI4OTksInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.v0znSZXe_tIl-X1sRR3Vz2737pfBAcgDUU8mjHeau6Q";
        ViewType viewType = ViewType.BACKGROUND_TASK;
        LogLevel logLevel = LogLevel.DEV;
        Map<String, Object> functionParams = new HashMap<>();
        String brandName = "Cooklist";
        String logoUrl = "https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ";
        String devApiLocation = "https://api.cooklist.com/gql";

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
