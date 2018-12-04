package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO if logged, don't start the below activity
        Intent intent = new Intent(MainActivity.this, HomeGuestActivity.class);
        startActivity(intent);
        finish();
    }

    private void connectionFailed() {
    }
}
