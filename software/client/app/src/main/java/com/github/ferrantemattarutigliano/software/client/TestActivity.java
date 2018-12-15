package com.github.ferrantemattarutigliano.software.client;
/*
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.httprequest.AuthorizationToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.WebSocket;
import rx.Subscriber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class TestActivity extends AppCompatActivity {
    private Button start;
    private TextView output;

    private static final String URL = "ws://10.0.0.2:8080/server";

    private StompClient mStompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }

    private void listenToUpdatesFromFinalUri(String content) {

        mStompClient.topic(content).subscribe(new Subscriber<StompMessage>() {

            @Override
            public void onCompleted() {
                System.out.println(" onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(" onError: " + e.getMessage());
            }

            @Override
            public void onNext(StompMessage stompMessage) {
                System.out.println(" onNext: " + stompMessage.getPayload());
            }
        });
    }


    private void start() {

        Map<String, String> headers = new HashMap<>();
        headers.put(AuthorizationToken.getAuthName(), AuthorizationToken.getAuthToken());

        mStompClient = Stomp.over(WebSocket.class, URL, headers);
        mStompClient.connect();

        mStompClient.topic("/request").subscribe(new Subscriber<StompMessage>() {
            @Override
            public void onCompleted() {
                output("/request onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                output("/request onError: " + e.getMessage());
            }

            @Override
            public void onNext(StompMessage stompMessage) {
                output("/request onNext: " + stompMessage.getPayload());
                String content = "";
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(stompMessage.getPayload());
                    content = jsonResponse.getString("uri");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listenToUpdatesFromFinalUri(content);
            }
        });

        //mStompClient.disconnect();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
} */