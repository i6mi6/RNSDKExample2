package com.storelink;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class StorelinkActivity extends AppCompatActivity {

    private StorelinkCore.SDKHandler handler;
    private String refreshToken = "your_refresh_token"; // You can set this from your config

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storelink);

        initializeSDK();
    }

    private void initializeSDK() {
        StorelinkCore.Configuration config = new StorelinkCore.Configuration(
                refreshToken,
                LogLevel.DEV,
                this::onConfigurationSuccess,
                this::onStoreConnectionEvent,
                this::onInvoiceEvent,
                this::onCheckingStoreConnectionEvent,
                "Cooklist",
                "https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ",
                "http://localhost:8000/gql"
        );

        handler = StorelinkCore.create(config);

        // Example of how to get a background view and add it to the activity
        launchStorelinkViewController();
    }

    private void launchStorelinkViewController() {
        Intent intent = StorelinkViewController.createIntent(
                this,
                refreshToken,
                ViewType.BACKGROUND_TASK,
                LogLevel.DEV,
                new HashMap<>(),
                null,
                "Cooklist",
                "https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ",
                "http://localhost:8000/gql"
        );
        startActivity(intent);
    }

    private void onConfigurationSuccess(Map<String, Object> params) {
        // Handle configuration success event
        System.out.println("Configuration Success: " + params.toString());
    }

    private void onStoreConnectionEvent(Map<String, Object> params) {
        // Handle store connection event
        System.out.println("Store Connection Event: " + params.toString());
    }

    private void onInvoiceEvent(Map<String, Object> params) {
        // Handle invoice event
        System.out.println("Invoice Event: " + params.toString());
    }

    private void onCheckingStoreConnectionEvent(Map<String, Object> params) {
        // Handle checking store connection event
        System.out.println("Checking Store Connection Event: " + params.toString());
    }
}
