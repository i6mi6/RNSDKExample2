//
//  StorelinkViewController.swift
//  Storelink
//
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum ViewType {
    BACKGROUND_TASK,
    STORE_CONNECTIONS_LIST,
    CONNECT_UPDATE_STORE,
    TRANSFER_CART,
    DEVICE_UUID;

    public String getStringValue() {
        switch (this) {
            case BACKGROUND_TASK:
                return "BACKGROUND_TASK";
            case STORE_CONNECTIONS_LIST:
                return "STORE_CONNECTIONS_LIST";
            case CONNECT_UPDATE_STORE:
                return "CONNECT_UPDATE_STORE";
            case TRANSFER_CART:
                return "TRANSFER_CART";
            case DEVICE_UUID:
                return "DEVICE_UUID";
            default:
                throw new IllegalArgumentException("Unknown ViewType");
        }
    }
}

public enum LogLevel {
    NONE,
    DEBUG,
    DEV;

    public int getIntValue() {
        switch (this) {
            case NONE:
                return 1;
            case DEBUG:
                return 2;
            case DEV:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown LogLevel");
        }
    }
}

public class StorelinkCoreActivity extends Activity {
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
        // Additional initialization code can be added here
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

    // Additional methods and logic can be added here
}
