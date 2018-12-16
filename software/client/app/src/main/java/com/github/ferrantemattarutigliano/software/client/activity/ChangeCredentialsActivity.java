package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.presenter.ChangeCredentialsPresenter;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.ChangeCredentialsView;

public class ChangeCredentialsActivity extends AppCompatActivity implements ChangeCredentialsView {
    private LoadingScreen loadingScreen;
    private ChangeCredentialsPresenter changeCredentialsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_credentials);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        changeCredentialsPresenter = new ChangeCredentialsPresenter(this);
        ViewGroup layout = findViewById(R.id.layout_change_credentials);
        loadingScreen = new LoadingScreen(layout, "Sending...");
        final TextView usernameText = findViewById(R.id.text_change_credentials_username);
        final TextView passwordText = findViewById(R.id.text_change_credentials_password);
        final TextView passwordRepeatText = findViewById(R.id.text_change_credentials_repeat_password);
        Button usernameButton = findViewById(R.id.button_change_credentials_username);
        Button passwordButton = findViewById(R.id.button_change_credentials_password);

        usernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                loadingScreen.show();
                changeCredentialsPresenter.doChangeUsername(username);
            }
        });
        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordText.getText().toString();
                String repeatPassword = passwordRepeatText.getText().toString();
                if(!password.equals(repeatPassword)){
                    createDialog("Password don't match", "Change password fail");
                    return;
                }
                loadingScreen.show();
                changeCredentialsPresenter.doChangePassword(password);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChangeUsernameSuccess(String output) {
        createDialog(output, "Change username success");
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isAutoLoginEnabled = sharedPreferences.contains("remember");
        if(isAutoLoginEnabled){
            TextView usernameText = findViewById(R.id.text_change_credentials_username);
            String username = usernameText.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();
        }
    }

    @Override
    public void onChangePasswordSuccess(String output) {
        createDialog(output, "Change password success");
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isAutoLoginEnabled = sharedPreferences.contains("remember");
        if(isAutoLoginEnabled){
            TextView passwordText = findViewById(R.id.text_change_credentials_password);
            String password = passwordText.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", password);
            editor.apply();
        }
    }

    @Override
    public void onChangeUsernameFail(String output) {
        createDialog(output, "Change username fail");
    }

    @Override
    public void onChangePasswordFail(String output) {
        createDialog(output, "Change password fail");
    }

    private void createDialog(String message, String title){
        loadingScreen.hide();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Okay", null);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.show();
    }
}
