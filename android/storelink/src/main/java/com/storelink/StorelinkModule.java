package com.storelink;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

public class StorelinkModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    // private static final String TAG = "StorelinkModule";
    private final ReactApplicationContext reactContext;

    public StorelinkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "StorelinkModule";
    }

    @ReactMethod
    public void sendNotification(String notification, ReadableMap params) {
        Map<String, Object> event = new HashMap<>();
        event.put("notification", notification);
        if (params != null) {
            Map<String, Object> paramsMap = params.toHashMap();
            for (String key : paramsMap.keySet()) {
                event.put(key, paramsMap.get(key));
            }
        }
        EventManager.getInstance().postEvent(event);
    }

    // private void sendEvent(String eventName, WritableMap params) {
    //     if (reactContext.hasActiveCatalystInstance()) {
    //         reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    //     } else {
    //         Log.w(TAG, "React context is not active, event not sent: " + eventName);
    //     }
    // }

    @Override
    public void onHostResume() {
        // Activity `onResume`
    }

    @Override
    public void onHostPause() {
        // Activity `onPause`
    }

    @Override
    public void onHostDestroy() {
        // Clean up resources
    }
}
