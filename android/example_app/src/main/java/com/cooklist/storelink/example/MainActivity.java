package com.cooklist.storelink.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.storelink.LogLevel;
import com.storelink.SDKHandler;

public class MainActivity extends AppCompatActivity {

    private SDKHandler sdkHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdkHandler = new SDKHandler(
                this,
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo5ODkxMzksIm9pZCI6MiwidmVyc2lvbiI6MSwianRpIjoiZjRkNGQzYTEtNzlkOC00MWQ3LTliMDYtZTk2MGU0MDFkODJmIiwiaXNzdWVkX2F0IjoxNzIyODk2NzE1LjIwMjg5OSwiZXhwaXJlc19hdCI6MTc1NDQzMjcxNS4yMDI4OTksInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.v0znSZXe_tIl-X1sRR3Vz2737pfBAcgDUU8mjHeau6Q",
                "Cooklist",
                "https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ",
                LogLevel.NONE
        );

        launchBackgroundView();

        Button buttonLaunchSecondActivity = findViewById(R.id.button_launch_second_activity);
        buttonLaunchSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchConnectUpdateStoreActivity("U3RvcmVOb2RlOjM=");
            }
        });
    }

    private void launchBackgroundView() {
        Fragment backgroundViewFragment = sdkHandler.getBackgroundView();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_background_container, backgroundViewFragment)
                .commit();
    }

    private void launchConnectUpdateStoreActivity(String storeId) {
        Intent intent = sdkHandler.getConnectUpdateStoreView(storeId);
        startActivity(intent);
    }
}
