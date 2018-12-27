package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualHomeActivity;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.ThirdPartyHomeActivity;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.LoginPresenter;
import com.github.ferrantemattarutigliano.software.client.util.Information;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.LoginView;

public class MainActivity extends AppCompatActivity implements LoginView {
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        goToLoginPage(null);
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
        goToLoginPage(output);
    }

    private void goToLoginPage(String output){
        if(output != null)
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
