package com.github.ferrantemattarutigliano.software.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompCallback;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompClient;
import com.github.ferrantemattarutigliano.software.client.websocket.payload.StompFrame;

public class TestActivity extends AppCompatActivity {
    private Button start;
    private TextView output;
    private StompClient stompClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);

        //init the client
        stompClient = new StompClient(new StompCallback() {
            @Override
            public void onResponseReceived(StompFrame response) {
                //here you can handle the response.
                //use response.getStompBody() to get the message received.

                //ignore below line, was used to test on screen
                output(new String(response.getStompBody().getBytes()));
            }
        });
        //connect after login. disconnect BEFORE logout.
        //after each connection you need to subscribe all previous topics again.
        //so you have to save them before disconnect. You can use shared preferences
        //to do this. (see login/registration activity to see the process)

        //send a plain text message
        //stompClient.send("/app/testsocket", "messageeeee");
        //send a json message (like a class in json)
        //stompClient.sendJson("/app/......", "jsonMessage");
        //unsubscribe a topic
        //stompClient.unsubscribe("/topic/test");
        //disconnect
        //stompClient.disconnect(); //REMEMBER TO DISCONNECT WHEN DONE.
        //check if websocket is connected
        //stompClient.isConnected();

        //connection
        stompClient.connect("ws://192.168.1.84:8080/server_endpoint");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //subscribe a topic
                stompClient.subscribe("/request/davvv");
            }
        });
    }

    @Override
    public void onDestroy() {
        stompClient.disconnect();
        super.onDestroy();
    }
    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
}