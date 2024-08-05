package com.storelink;
import java.util.Map;

// Interface for event listeners
public interface OnEventListener {
    void onEvent(Map<String, Object> params);
}