package com.storelink;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class StorelinkModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private static final String TAG = "StorelinkModule";
    private static final String EVENT_NAME = "CooklistDataFromNative";
    private final ReactApplicationContext reactContext;

    public StorelinkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addLifecycleEventListener(this);
        setupLiveDataObserver();
    }

    @Override
    public String getName() {
        return "StorelinkModule";
    }

    @ReactMethod
    public void sendNotification(String notification, WritableMap params) {
//        Map<String, Object> event = new HashMap<>();
//        event.put("notification", notification);
//        if (params != null) {
//            Map<String, Object> paramsMap = params.toHashMap();
//            for (String key : paramsMap.keySet()) {
//                event.put(key, paramsMap.get(key));
//            }
//        }
//        EventManager.getInstance().postEvent(event);
    }

    private void setupLiveDataObserver() {
//        Handler mainHandler = new Handler(Looper.getMainLooper());
//        mainHandler.post(() -> {
//            EventManager.getInstance().getEventLiveData().observeForever(new Observer<Map<String, Object>>() {
//                @Override
//                public void onChanged(Map<String, Object> event) {
//                    if (event != null && event.containsKey("notification")) {
//                        WritableMap params = new WritableNativeMap();
//                        for (Map.Entry<String, Object> entry : event.entrySet()) {
//                            params.putString(entry.getKey(), entry.getValue().toString());
//                        }
//                        sendEvent(EVENT_NAME, params);
//                    }
//                }
//            });
//        });
    }

    private void sendEvent(String eventName, WritableMap params) {
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
        } else {
            Log.w(TAG, "React context is not active, event not sent: " + eventName);
        }
    }

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
