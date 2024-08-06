package com.cooklist.storelink.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.storelink.ConnectOrUpdateStoreActivity;
import com.storelink.StorelinkFragment;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        launchBackgroundView();

        Button buttonLaunchSecondActivity = findViewById(R.id.button_launch_second_activity);
        buttonLaunchSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchConnectOrUpdateStoreActivity();
            }
        });
    }

    private void launchBackgroundView() {
        Bundle args = new Bundle();
        args.putString("refreshToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo5ODkxMzksIm9pZCI6MiwidmVyc2lvbiI6MSwianRpIjoiZjRkNGQzYTEtNzlkOC00MWQ3LTliMDYtZTk2MGU0MDFkODJmIiwiaXNzdWVkX2F0IjoxNzIyODk2NzE1LjIwMjg5OSwiZXhwaXJlc19hdCI6MTc1NDQzMjcxNS4yMDI4OTksInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.v0znSZXe_tIl-X1sRR3Vz2737pfBAcgDUU8mjHeau6Q");
        args.putString("viewType", "BACKGROUND_TASK");
        args.putString("logLevel", "DEV");
        args.putString("brandName", "Cooklist");
        args.putString("logoUrl", "https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ");
        args.putString("devApiLocation", "https://api.cooklist.com/gql");

        HashMap<String, String> functionParams = new HashMap<>();
        functionParams.put("key1", "value1");
        args.putSerializable("functionParams", functionParams);

        StorelinkFragment fragment = new StorelinkFragment();
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_background_container, fragment)
            .commit();
    }

    private void launchConnectOrUpdateStoreActivity() {
        Intent intent = new Intent(this, ConnectOrUpdateStoreActivity.class);

        intent.putExtra("refreshToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo5ODkxMzksIm9pZCI6MiwidmVyc2lvbiI6MSwianRpIjoiZjRkNGQzYTEtNzlkOC00MWQ3LTliMDYtZTk2MGU0MDFkODJmIiwiaXNzdWVkX2F0IjoxNzIyODk2NzE1LjIwMjg5OSwiZXhwaXJlc19hdCI6MTc1NDQzMjcxNS4yMDI4OTksInRva2VuX3R5cGUiOiJyZWZyZXNoIn0.v0znSZXe_tIl-X1sRR3Vz2737pfBAcgDUU8mjHeau6Q");
        intent.putExtra("viewType", "CONNECT_UPDATE_STORE");
        intent.putExtra("logLevel", "DEV");
        intent.putExtra("brandName", "Cooklist");
        intent.putExtra("logoUrl", "https://play-lh.googleusercontent.com/1MgS_1nBA858MqMzhu-cqeXpbkTC3tVrshkj79IAuKhDlN7LZXdH4ECw6wiwA86vUQ");
        intent.putExtra("devApiLocation", "https://api.cooklist.com/gql");

        HashMap<String, String> functionParams = new HashMap<>();
        functionParams.put("storeId", "U3RvcmVOb2RlOjM=");
        intent.putExtra("functionParams", functionParams);

        startActivity(intent);
    }
}
