//
//  StorelinkCoreView.swift
//  Storelink
//
import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.Map;

public class StorelinkCoreView {
    private String refreshToken;
    private LogLevel logLevel;
    private ViewType viewType;
    private Map<String, Object> functionParams;
    private OnCompleteListener onComplete;
    private String brandName;
    private String logoUrl;
    private String devApiLocation;

    public StorelinkCoreView(String refreshToken,
                             @Nullable LogLevel logLevel,
                             ViewType viewType,
                             @Nullable Map<String, Object> functionParams,
                             @Nullable OnCompleteListener onComplete,
                             @Nullable String brandName,
                             @Nullable String logoUrl,
                             @Nullable String devApiLocation) {
        this.refreshToken = refreshToken;
        this.logLevel = logLevel;
        this.viewType = viewType;
        this.functionParams = functionParams;
        this.onComplete = onComplete;
        this.brandName = brandName;
        this.logoUrl = logoUrl;
        this.devApiLocation = devApiLocation;
    }

    public View createView(@NonNull Context context) {
        FragmentActivity activity = (FragmentActivity) context;
        StorelinkCore.SDKHandler.SDKFragment sdkFragment = StorelinkCore.SDKHandler.SDKFragment.newInstance(
                refreshToken,
                viewType,
                logLevel,
                functionParams,
                onComplete,
                brandName,
                logoUrl,
                devApiLocation
        );

        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, sdkFragment)
                .commit();

        return new View(context);
    }

    public void updateView(View view) {
        // Update code if needed
    }

    public interface OnCompleteListener {
        void onComplete(Map<String, Object> data);
    }

    public enum ViewType {
        BACKGROUND_TASK,
        TRANSFER_CART
        // Add other view types as needed
    }
}
