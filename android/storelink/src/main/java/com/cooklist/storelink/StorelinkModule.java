package com.cooklist.storelink;
import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;

public class StorelinkModule extends ReactContextBaseJavaModule {
   StorelinkModule(ReactApplicationContext context) {
       super(context);
   }

   @Override
    public String getName() {
        return "StorelinkModule";
    }
    
    @ReactMethod
    public void testStorelinkModule(String text) {
        Log.d("StorelinkModule", "Test event called with name: " + text);
    }
}