package com.storelinksdk;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.BuildConfig;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.soloader.SoLoader;
import com.facebook.react.PackageList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class StorelinkCoreActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    private LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
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

    //
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    public interface OnCompleteListener {
        void onComplete(Map<String, Object> data);
    }

    public StorelinkCoreActivity(String refreshToken, ViewType viewType, @Nullable LogLevel logLevel,
                                 @Nullable Map<String, Object> functionParams,
                                 @Nullable OnCompleteListener onComplete,
                                 @Nullable String brandName, @Nullable String logoUrl,
                                 @Nullable String devApiLocation) {
        this.refreshToken = refreshToken;
        this.viewType = viewType;
        this.logLevel = logLevel;
        this.functionParams = functionParams;
        this.onComplete = onComplete;
        this.brandName = brandName;
        this.logoUrl = logoUrl;
        this.devApiLocation = devApiLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onViewUnmount();
    }

    private void onViewUnmount() {
        if (viewType == ViewType.CONNECT_UPDATE_STORE && functionParams != null && functionParams.containsKey("storeId")) {
            String storeId = (String) functionParams.get("storeId");
            Map<String, Object> data = new HashMap<>();
            data.put("_cooklistInternal", true);
            data.put("eventType", "cooklist_sdk_event_eager_check_bg_purchases");
            data.put("storeId", storeId);
            // You would need to implement a way to broadcast this data, similar to NotificationCenter in iOS
            // For example, you could use LocalBroadcastManager or create a custom event bus
        }
    }

    private void setupView() {
        SoLoader.init(this, false);

        mReactRootView = new ReactRootView(this);
        List<ReactPackage> packages = new PackageList(getApplication()).getPackages();

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                .setBundleAssetName("storelink.jsbundle")
                .setJSMainModulePath("index")
                .addPackages(packages)
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

        mReactRootView.startReactApplication(mReactInstanceManager, "StorelinSDK", null);

        setContentView(mReactRootView);
    }

    // Additional methods and logic can be added here
}
