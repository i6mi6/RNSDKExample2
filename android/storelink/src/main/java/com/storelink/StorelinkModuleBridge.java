package com.storelink;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class StorelinkModuleBridge extends ReactContextBaseJavaModule {

    public StorelinkModuleBridge(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "StorelinkModuleBridge";
    }

    @ReactMethod
    public void sampleMethod(String message, Callback callback) {
        // Implement method to call native functionality
        callback.invoke("Received: " + message);
    }
}