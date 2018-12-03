package com.github.ferrantemattarutigliano.software.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.task.LoginTask;
import com.github.ferrantemattarutigliano.software.client.task.RegisterTask;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //example with setting parameters in UI
        final Button loginButton = findViewById(R.id.login_button);
        final Button registerButton = findViewById(R.id.register_button);
        final TextView emailForm = findViewById(R.id.username_textview);
        final TextView passwordForm = findViewById(R.id.password_textview);
        final TextView test = findViewById(R.id.test_textview);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDTO user = new UserDTO(emailForm.getText().toString(), passwordForm.getText().toString());
                new LoginTask(user, new AsyncResponse<String>() {
                    @Override
                    public void taskFinish(String output) {
                        test.setText(output);
                    }

                    @Override
                    public void taskFail() {
                        Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                }).execute();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDTO user = new UserDTO(emailForm.getText().toString(), passwordForm.getText().toString());
                new RegisterTask(user, new AsyncResponse<String>() {
                    @Override
                    public void taskFinish(String output) {
                        test.setText(output);
                    }

                    @Override
                    public void taskFail() {
                        Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                }).execute();

            }
        });
    }
}
