package com.storelink;

import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import java.util.HashMap;

public class StorelinkFragment extends Fragment implements DefaultHardwareBackBtnHandler {
    private ReactNativeFragmentManager mReactNativeFragmentManager;
    private ReactRootView mReactRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            String refreshToken = getArguments().getString("refreshToken");
            ViewType viewType = ViewType.valueOf(getArguments().getString("viewType"));

            LogLevel logLevel = null;
            if (getArguments().containsKey("logLevel")) {
                logLevel = LogLevel.values()[getArguments().getInt("logLevel") - 1];
            }

            String brandName = getArguments().getString("brandName");
            String logoUrl = getArguments().getString("logoUrl");
            String devApiLocation = getArguments().getString("devApiLocation");
            HashMap<String, String> functionParams = (HashMap<String, String>) getArguments().getSerializable("functionParams");

            mReactNativeFragmentManager = new ReactNativeFragmentManager(requireActivity().getApplication(), requireActivity(), this);
            mReactRootView = mReactNativeFragmentManager.createReactNativeView(requireActivity(), refreshToken, viewType, logLevel, functionParams, brandName, logoUrl, devApiLocation);
            return mReactRootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mReactNativeFragmentManager.onPause(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mReactNativeFragmentManager.onResume(requireActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReactNativeFragmentManager.onDestroy(requireActivity());
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        requireActivity().onBackPressed();
    }
}
