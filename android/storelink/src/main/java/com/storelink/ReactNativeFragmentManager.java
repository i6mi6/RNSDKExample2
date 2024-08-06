package com.storelink;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.soloader.SoLoader;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactNativeFragmentManager {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private DefaultHardwareBackBtnHandler mDefaultBackButtonHandler;

    public ReactNativeFragmentManager(Application application, Activity activity, DefaultHardwareBackBtnHandler defaultBackButtonHandler) {
        SoLoader.init(application, false);
        this.mDefaultBackButtonHandler = defaultBackButtonHandler;

        List<ReactPackage> packages = new PackageList(application).getPackages();
        packages.add(new StorelinkPackage());

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setCurrentActivity(activity)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackages(packages)
                .setUseDeveloperSupport(false)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .setJavaScriptExecutorFactory(new HermesExecutorFactory())
                .build();
    }

    public ReactRootView createReactNativeView(Activity activity, String refreshToken, ViewType viewType, LogLevel logLevel,
                                               HashMap<String, String> functionParams, String brandName, String logoUrl, String devApiLocation) {
        mReactRootView = new ReactRootView(activity);

        Bundle initialProps = new Bundle();
        initialProps.putString("refreshToken", refreshToken);
        initialProps.putInt("logLevel", logLevel.getIntValue());
        initialProps.putString("viewType", viewType.getStringValue());
        initialProps.putString("brandName", brandName);
        initialProps.putString("logoUrl", logoUrl);

        if (functionParams != null) {
            Bundle functionParamsBundle = new Bundle();
            for (Map.Entry<String, String> entry : functionParams.entrySet()) {
                functionParamsBundle.putString(entry.getKey(), entry.getValue());
            }
            initialProps.putBundle("functionParams", functionParamsBundle);
        }

        mReactRootView.startReactApplication(mReactInstanceManager, "StorelinkProject", initialProps);
        return mReactRootView;
    }

    public void onPause(Activity activity) {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(activity);
        }
    }

    public void onResume(Activity activity) {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(activity, mDefaultBackButtonHandler);
        }
    }

    public void onDestroy(Activity activity) {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(activity);
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        }
    }

    public boolean onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return false;
    }
}
