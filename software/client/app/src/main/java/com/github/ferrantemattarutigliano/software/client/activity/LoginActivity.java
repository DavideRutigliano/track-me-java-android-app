package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualHomeActivity;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.ThirdPartyHomeActivity;
import com.github.ferrantemattarutigliano.software.client.httprequest.HttpOutputMessage;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.LoginPresenter;
import com.github.ferrantemattarutigliano.software.client.util.Information;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
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
        final CheckBox rememberCheck = findViewById(R.id.check_remember_me);
        final SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        loadingScreen = new LoadingScreen(container, "Logging in...");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameForm.getText().toString();
                String password = passwordForm.getText().toString();
                loadingScreen.show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (rememberCheck.isChecked()) {
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("remember", true);
                }
                else
                    editor.clear();
                editor.apply();
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
        //if remember me it's active, add the latest login data
        //in case autologin doesn't succeed
        if(sharedPreferences.contains("remember")){
            CharSequence username = sharedPreferences.getString("username", "");
            CharSequence password = sharedPreferences.getString("password", "");
            usernameForm.setText(username);
            passwordForm.setText(password);
            rememberCheck.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoginSuccess(UserDTO userDTO) {
        Intent intent;
        switch (userDTO.getRole()){
            case "INDIVIDUAL":
                intent = new Intent(this, IndividualHomeActivity.class);
                break;
            case "THIRD_PARTY":
                intent = new Intent(this, ThirdPartyHomeActivity.class);
                break;
            default:
                throw new RuntimeException(Information.ROLE_NOT_FOUND.toString());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFail(String output) {
        loadingScreen.hide();
        //alert user that login failed
        AlertDialog.Builder dialogFactory = new AlertDialog.Builder(this);
        dialogFactory.setTitle("Login Failed")
                .setMessage(output)
                .setPositiveButton("Okay :(", null)
                .show();
        //don't delete "remember me" info in case of timeout
        if(output.equals(HttpOutputMessage.TIMEOUT.toString()))
            return;
        //remove "remember me" info
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
