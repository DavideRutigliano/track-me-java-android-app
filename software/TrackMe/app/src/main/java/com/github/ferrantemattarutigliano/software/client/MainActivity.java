package com.github.ferrantemattarutigliano.software.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //example with setting parameters in UI
        Button restButton = findViewById(R.id.rest_test_button);
        restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequestTask httpRequestTask = new HttpRequestTask();
                httpRequestTask.execute();
                String result = httpRequestTask.doInBackground();

                TextView textView = findViewById(R.id.example_text); //get text reference
                textView.setText(result);
            }
        });
    }

}
