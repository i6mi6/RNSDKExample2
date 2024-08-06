package com.storelink;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.storelink.StorelinkFragment;
import java.util.HashMap;

public class ConnectOrUpdateStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storelink);

        if (savedInstanceState == null) {
            Bundle args = getIntent().getExtras();

            StorelinkFragment fragment = new StorelinkFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        }
    }
}
