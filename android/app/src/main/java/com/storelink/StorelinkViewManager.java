package com.storelink;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class StorelinkViewManager extends SimpleViewManager<StorelinkView> {

    public static final String REACT_CLASS = "StorelinkView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected StorelinkView createViewInstance(ThemedReactContext reactContext) {
        return new StorelinkView(reactContext);
    }
}
