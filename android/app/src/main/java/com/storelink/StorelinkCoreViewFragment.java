package com.storelink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorelinkCoreViewFragment extends Fragment {

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    private static final String ARG_REFRESH_TOKEN = "refreshToken";
    private static final String ARG_VIEW_TYPE = "viewType";
    private static final String ARG_LOG_LEVEL = "logLevel";
    private static final String ARG_FUNCTION_PARAMS = "functionParams";
    private static final String ARG_BRAND_NAME = "brandName";
    private static final String ARG_LOGO_URL = "logoUrl";
    private static final String ARG_DEV_API_LOCATION = "_devApiLocation";

    private static final String VIEW_COMPLETE_NOTIFICATION = "cooklist_sdk_view_complete_event";
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private String viewUUID;

    public static StorelinkCoreViewFragment newInstance(String refreshToken, StorelinkCore.ViewType viewType, StorelinkCore.LogLevel logLevel, Bundle functionParams, String brandName, String logoUrl, String devApiLocation) {
        StorelinkCoreViewFragment fragment = new StorelinkCoreViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REFRESH_TOKEN, refreshToken);
        args.putSerializable(ARG_VIEW_TYPE, viewType);
        args.putSerializable(ARG_LOG_LEVEL, logLevel);
        args.putBundle(ARG_FUNCTION_PARAMS, functionParams);
        args.putString(ARG_BRAND_NAME, brandName);
        args.putString(ARG_LOGO_URL, logoUrl);
        args.putString(ARG_DEV_API_LOCATION, devApiLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mReactRootView = new ReactRootView(getActivity());
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getActivity().getApplication())
                .setCurrentActivity(getActivity())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

        // Generate a UUID for the view
        viewUUID = UUID.randomUUID().toString();

        // Retrieve arguments
        Bundle args = getArguments();
        if (args != null) {
            String refreshToken = args.getString(ARG_REFRESH_TOKEN);
            StorelinkCore.ViewType viewType = (StorelinkCore.ViewType) args.getSerializable(ARG_VIEW_TYPE);
            StorelinkCore.LogLevel logLevel = (StorelinkCore.LogLevel) args.getSerializable(ARG_LOG_LEVEL);
            Bundle functionParams = args.getBundle(ARG_FUNCTION_PARAMS);
            String brandName = args.getString(ARG_BRAND_NAME);
            String logoUrl = args.getString(ARG_LOGO_URL);
            String devApiLocation = args.getString(ARG_DEV_API_LOCATION);

            // Create initial properties to pass to the React Native app
            WritableMap initialProps = Arguments.createMap();
            initialProps.putString("refreshToken", refreshToken);
            initialProps.putString("viewType", viewType.toString());
            initialProps.putString("logLevel", logLevel.toString());
            initialProps.putString("viewUUID", viewUUID);
            if (functionParams != null) {
                WritableMap functionParamsMap = Arguments.fromBundle(functionParams);
                initialProps.putMap("functionParams", functionParamsMap);
            }
            initialProps.putString("brandName", brandName);
            initialProps.putString("logoUrl", logoUrl);
            initialProps.putString("_devApiLocation", devApiLocation);

            // Convert WritableMap to Bundle
            Bundle initialPropsBundle = convertWritableMapToBundle(initialProps);

            // Pass initial properties to the React Native app
            mReactRootView.startReactApplication(mReactInstanceManager, "StorelinkProject", initialPropsBundle);
        }

        // Set up LocalBroadcastManager for notifications
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    WritableMap params = Arguments.fromBundle(extras);
                    String notificationUUID = params.getString("viewUUID");
                    if (notificationUUID != null && notificationUUID.equals(viewUUID)) {
                        // Handle the notification
                        // For example, call onComplete callback
                        // onComplete(params);
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(VIEW_COMPLETE_NOTIFICATION);
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);

        return mReactRootView;
    }

    private Bundle convertWritableMapToBundle(WritableMap map) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : map.toHashMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof Integer) {
                bundle.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (Double) value);
            } else if (value instanceof WritableMap) {
                bundle.putBundle(key, convertWritableMapToBundle((WritableMap) value));
            }
            // Add other types as needed
        }
        return bundle;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(getActivity(), (DefaultHardwareBackBtnHandler) getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(getActivity());
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
        if (localBroadcastManager != null && broadcastReceiver != null) {
            localBroadcastManager.unregisterReceiver(broadcastReceiver);
        }
    }

    public void sendDataToReactNative(Map<String, Object> data) {
        Intent intent = new Intent(VIEW_COMPLETE_NOTIFICATION);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            }
            // Add other types as needed
        }
        localBroadcastManager.sendBroadcast(intent);
    }
}
