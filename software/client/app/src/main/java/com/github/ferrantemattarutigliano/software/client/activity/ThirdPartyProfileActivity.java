package com.github.ferrantemattarutigliano.software.client.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.ferrantemattarutigliano.software.client.R;

public class ThirdPartyProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_profile);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
