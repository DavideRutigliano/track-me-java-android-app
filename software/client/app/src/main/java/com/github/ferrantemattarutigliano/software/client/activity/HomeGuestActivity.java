package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.presenter.HomeGuestPresenter;
import com.github.ferrantemattarutigliano.software.client.view.HomeGuestView;

public class HomeGuestActivity extends AppCompatActivity implements HomeGuestView {
    private HomeGuestPresenter homeGuestPresenter;
    private TextView connectionResultText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_guest);
        homeGuestPresenter = new HomeGuestPresenter(this);

        final Button loginButton = findViewById(R.id.button_login);
        final Button registerButton = findViewById(R.id.button_register);
        final TextView usernameForm = findViewById(R.id.text_registration_individual_username);
        final TextView passwordForm = findViewById(R.id.text_registration_individual_password);
        connectionResultText = findViewById(R.id.text_login_welcome);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameForm.getText().toString();
                String password = passwordForm.getText().toString();
                homeGuestPresenter.doLogin(username, password);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeGuestActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoginSuccess(String output) {
        connectionResultText.setText(output);
    }

    @Override
    public void onLoginFail() {
        connectionFailed();
    }

    private void connectionFailed(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
    }
}
