package com.storelink;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.Map;

public class StorelinkCoreViewFragment extends Fragment {

    private static final String ARG_REFRESH_TOKEN = "refreshToken";
    private static final String ARG_LOG_LEVEL = "logLevel";
    private static final String ARG_VIEW_TYPE = "viewType";
    private static final String ARG_FUNCTION_PARAMS = "functionParams";

    public static StorelinkCoreViewFragment newInstance(String refreshToken, LogLevel logLevel, ViewType viewType, Map<String, Object> functionParams) {
        StorelinkCoreViewFragment fragment = new StorelinkCoreViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REFRESH_TOKEN, refreshToken);
        args.putInt(ARG_LOG_LEVEL, logLevel.getIntValue());
        args.putString(ARG_VIEW_TYPE, viewType.getStringValue());
        args.putSerializable(ARG_FUNCTION_PARAMS, (Serializable) functionParams);
        fragment.setArguments(args);
        return fragment;
    }

    public static StorelinkCoreViewFragment newInstance(String refreshToken, LogLevel logLevel, ViewType viewType, Map<String, Object> functionParams, OnCompleteListener onComplete) {
        StorelinkCoreViewFragment fragment = new StorelinkCoreViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REFRESH_TOKEN, refreshToken);
        args.putInt(ARG_LOG_LEVEL, logLevel.getIntValue());
        args.putString(ARG_VIEW_TYPE, viewType.getStringValue());
        args.putSerializable(ARG_FUNCTION_PARAMS, (Serializable) functionParams);
        fragment.setArguments(args);
        fragment.setOnCompleteListener(onComplete);
        return fragment;
    }

    private OnCompleteListener onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String refreshToken = getArguments().getString(ARG_REFRESH_TOKEN);
            int logLevel = getArguments().getInt(ARG_LOG_LEVEL);
            String viewType = getArguments().getString(ARG_VIEW_TYPE);
            Map<String, Object> functionParams = (Map<String, Object>) getArguments().getSerializable(ARG_FUNCTION_PARAMS);
            // Handle the view initialization
        }
    }
}
