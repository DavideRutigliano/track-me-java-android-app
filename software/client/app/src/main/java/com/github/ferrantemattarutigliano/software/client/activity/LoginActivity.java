package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.Information;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualHomeActivity;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.ThirdPartyHomeActivity;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.LoginPresenter;
import com.github.ferrantemattarutigliano.software.client.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private LoginPresenter loginPresenter;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenter(this);

        final ConstraintLayout container = findViewById(R.id.container_login);
        final Button loginButton = findViewById(R.id.button_login);
        final Button signupButton = findViewById(R.id.button_register);
        final TextView usernameForm = findViewById(R.id.text_login_username);
        final TextView passwordForm = findViewById(R.id.text_login_password);

        loadingScreen = new LoadingScreen(container, "Logging in...");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameForm.getText().toString();
                String password = passwordForm.getText().toString();
                loadingScreen.show();
                loginPresenter.doLogin(username, password);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
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
        Intent intent;
        if(userDTO.getRole().equals("INDIVIDUAL")){
            intent = new Intent(this, IndividualHomeActivity.class);
        }
        else if(userDTO.getRole().equals("THIRD_PARTY")){
            intent = new Intent(this, ThirdPartyHomeActivity.class);
        }
        else{
            throw new RuntimeException(Information.ROLE_NOT_FOUND.toString());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFail(String output) {
        loadingScreen.hide();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Okay :(", null);
        alertDialogBuilder.setTitle("Login Failed");
        alertDialogBuilder.setMessage(output);
        alertDialogBuilder.show();
    }
}
