package com.cooklist.storelink.example;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.storelink.StorelinkActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchStorelinkActivity();
    }

    private void launchStorelinkActivity() {
        Intent intent = new Intent(getApplicationContext(), StorelinkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextCompat.startActivity(this, intent, null);
    }
}