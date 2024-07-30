package com.storelink;

import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class StorelinkModuleBridge extends ReactContextBaseJavaModule {

    private static final String EVENT_NAME = "CooklistDataFromNative";
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    public StorelinkModuleBridge(ReactApplicationContext reactContext) {
        super(reactContext);
        localBroadcastManager = LocalBroadcastManager.getInstance(reactContext);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    WritableMap params = Arguments.fromBundle(extras);
                    sendEvent(EVENT_NAME, params);
                }
            }
        };

        IntentFilter filter = new IntentFilter(EVENT_NAME);
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public String getName() {
        return "StorelinkModule";
    }

    @ReactMethod
    public void sendNotification(String notification, ReadableMap params, Promise promise) {
        Intent intent = new Intent(notification);
        Bundle bundle = new Bundle();

        ReadableMapKeySetIterator iterator = params.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            switch (params.getType(key)) {
                case String:
                    bundle.putString(key, params.getString(key));
                    break;
                case Number:
                    // Assuming all numbers are integers. You might need to handle double, float, etc.
                    bundle.putDouble(key, params.getDouble(key));
                    break;
                case Boolean:
                    bundle.putBoolean(key, params.getBoolean(key));
                    break;
                // Add other types if needed
            }
        }
        
        intent.putExtras(bundle);
        localBroadcastManager.sendBroadcast(intent);
        promise.resolve(null);
    }

    private void sendEvent(String eventName, WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
