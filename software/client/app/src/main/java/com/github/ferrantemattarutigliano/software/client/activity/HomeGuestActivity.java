package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.Information;
import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.individual.HomeIndividualActivity;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.HomeThirdPartyActivity;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.HomeGuestPresenter;
import com.github.ferrantemattarutigliano.software.client.view.HomeGuestView;

public class HomeGuestActivity extends AppCompatActivity implements HomeGuestView {
    private HomeGuestPresenter homeGuestPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_guest);
        homeGuestPresenter = new HomeGuestPresenter(this);

        final Button loginButton = findViewById(R.id.button_login);
        final Button registerButton = findViewById(R.id.button_register);
        final TextView usernameForm = findViewById(R.id.text_registration_individual_username);
        final TextView passwordForm = findViewById(R.id.text_registration_individual_password);

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
    public void onLoginSuccess(UserDTO userDTO) {
        Intent intent = null;
        if(userDTO.getRole().equals("INDIVIDUAL")){
            intent = new Intent(this, HomeIndividualActivity.class);
        }
        else if(userDTO.getRole().equals("THIRD_PARTY")){
            intent = new Intent(this, HomeThirdPartyActivity.class);
        }
        else{
            throw new RuntimeException(Information.ROLE_NOT_FOUND.toString());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFail(String output) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Ok :(", null);
        alertDialogBuilder.setTitle("Login Failed");
        alertDialogBuilder.setMessage(output);
        alertDialogBuilder.show();
    }
}
