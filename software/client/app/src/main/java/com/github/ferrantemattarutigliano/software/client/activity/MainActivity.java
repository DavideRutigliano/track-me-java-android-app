package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.github.ferrantemattarutigliano.software.client.util.Information;
import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualHomeActivity;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.ThirdPartyHomeActivity;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.LoginPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.LoginView;

public class MainActivity extends AppCompatActivity implements LoginView {
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        ViewGroup layout = findViewById(R.id.layout_main);
        LoadingScreen loadingScreen = new LoadingScreen(layout, "Loading data...");
        loadingScreen.show();
        loginPresenter = new LoginPresenter(this);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isAutoLoginEnabled = sharedPreferences.contains("remember");

        if(isAutoLoginEnabled){
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");
            loginPresenter.doLogin(username, password);
            return;
        }
        goToLoginPage();
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
        goToLoginPage();
    }

    private void goToLoginPage(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
