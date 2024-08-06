package com.storelink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.shell.MainReactPackage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StorelinkViewController extends AppCompatActivity {

    private static final String TAG = "StorelinkViewController";
    private String viewUUID = UUID.randomUUID().toString();
    private String viewCompleteNotificationName = "cooklist_sdk_view_complete_event";

    private String refreshToken;
    private ViewType viewType;
    private LogLevel logLevel;
    private Map<String, Object> functionParams;
    private OnCompleteListener onComplete;
    private String brandName;
    private String logoUrl;
    private String devApiLocation;

    private ReactRootView reactRootView;
    private ReactInstanceManager reactInstanceManager;

    public interface OnCompleteListener {
        void onComplete(Map<String, Object> result);
    }

    public static Intent createIntent(Context context, String refreshToken, ViewType viewType, LogLevel logLevel, Map<String, Object> functionParams, OnCompleteListener onComplete, String brandName, String logoUrl, String devApiLocation) {
        Intent intent = new Intent(context, StorelinkViewController.class);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("viewType", viewType.getStringValue());
        intent.putExtra("logLevel", logLevel.getIntValue());
        intent.putExtra("functionParams", new HashMap<>(functionParams));
        intent.putExtra("brandName", brandName);
        intent.putExtra("logoUrl", logoUrl);
        intent.putExtra("devApiLocation", devApiLocation);
        // Store OnCompleteListener in a static field or use a more sophisticated mechanism
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        refreshToken = intent.getStringExtra("refreshToken");
        viewType = ViewType.valueOf(intent.getStringExtra("viewType"));
        int logLevelIndex = intent.getIntExtra("logLevel", 1);

        // Ensure the logLevelIndex is within the range of LogLevel values
        if (logLevelIndex >= 0 && logLevelIndex < LogLevel.values().length) {
            logLevel = LogLevel.values()[logLevelIndex];
        } else {
            logLevel = LogLevel.NONE; // or set a default value
        }

        functionParams = (Map<String, Object>) intent.getSerializableExtra("functionParams");
        brandName = intent.getStringExtra("brandName");
        logoUrl = intent.getStringExtra("logoUrl");
        devApiLocation = intent.getStringExtra("devApiLocation");

        setupReactInstance();
        setupNotificationReceiver();
        loadReactNativeView();
    }

    private void setupReactInstance() {
        reactRootView = new ReactRootView(this);
        List<ReactPackage> packages = new PackageList(getApplication()).getPackages();
        packages.add(new StorelinkPackage());

        reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(new MainReactPackage())
                .addPackage(new StorelinkPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
    }

    private void setupNotificationReceiver() {
        // Assuming you have a mechanism to handle notifications
        EventManager.getInstance().getEventLiveData().observe(this, event -> {
            if (event != null && event.containsKey("viewUUID") && event.get("viewUUID").equals(viewUUID)) {
                onComplete.onComplete(event);
            }
        });
    }

    private void loadReactNativeView() {
        Bundle initialProps = new Bundle();
        initialProps.putString("refreshToken", refreshToken);
        initialProps.putInt("logLevel", logLevel.getIntValue());
        initialProps.putString("viewType", viewType.getStringValue());
        initialProps.putString("viewUUID", viewUUID);
        initialProps.putString("brandName", brandName);
        initialProps.putString("logoUrl", logoUrl);
        initialProps.putString("_devApiLocation", devApiLocation);

        if (functionParams != null) {
            for (Map.Entry<String, Object> entry : functionParams.entrySet()) {
                initialProps.putString(entry.getKey(), entry.getValue().toString());
            }
        }

        reactRootView.startReactApplication(reactInstanceManager, "StorelinkProject", initialProps);
        setContentView(reactRootView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reactInstanceManager != null) {
            reactInstanceManager.onHostDestroy(this);
        }
        if (reactRootView != null) {
            reactRootView.unmountReactApplication();
        }
        onViewUnmount();
    }

    private void onViewUnmount() {
        if (viewType == ViewType.CONNECT_UPDATE_STORE && functionParams != null) {
            String storeId = (String) functionParams.get("storeId");
            if (storeId != null) {
                WritableMap data = Arguments.createMap();
                data.putBoolean("_cooklistInternal", true);
                data.putString("eventType", "cooklist_sdk_event_eager_check_bg_purchases");
                data.putString("storeId", storeId);
                reactInstanceManager.getCurrentReactContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("CooklistDataFromNative", data);
            }
        }
    }
}
