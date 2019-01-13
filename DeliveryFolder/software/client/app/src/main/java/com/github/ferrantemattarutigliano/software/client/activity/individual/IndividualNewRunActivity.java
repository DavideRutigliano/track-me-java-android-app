package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.DateActivity;
import com.github.ferrantemattarutigliano.software.client.activity.TimeActivity;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualNewRunPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualNewRunView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class IndividualNewRunActivity extends AppCompatActivity implements IndividualNewRunView {
    private final int MAP_CODE = 100;
    private final int DATE_CODE = 101;
    private final int TIME_CODE = 102;
    private IndividualNewRunPresenter individualNewRunPresenter;
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_new_run);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar

        individualNewRunPresenter = new IndividualNewRunPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);
        ViewGroup layout = findViewById(R.id.layout_individual_new_run);
        loadingScreen = new LoadingScreen(layout);
        loadingScreen.changeMessage("Sending...");
        final TextView titleText = findViewById(R.id.text_individual_new_run_title);

        ViewGroup dateContainer = findViewById(R.id.container_individual_new_run_date);
        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DateActivity.class);
                startActivityForResult(intent, DATE_CODE);
            }
        });
        ViewGroup timeContainer = findViewById(R.id.container_individual_new_run_time);
        timeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
                startActivityForResult(intent, TIME_CODE);
            }
        });

        ImageButton mapButton = findViewById(R.id.image_button_individual_new_run_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IndividualCreateMapActivity.class);
                if(individualNewRunPresenter.isPathSet()){
                    ArrayList<PositionDTO> path = individualNewRunPresenter.getPositionDTOS();
                    intent.putExtra("path", path);
                }
                startActivityForResult(intent, MAP_CODE);
            }
        });

        Button confirmButton = findViewById(R.id.button_individual_new_run_create);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingScreen.show();
                String title = titleText.getText().toString();
                individualNewRunPresenter.doCreateRun(title);
            }
        });
    }

    @Override
    public void onCreateRunSuccess(final String message) {
        loadingScreen.hide();
        dialogFactory.setTitle("Information")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(message.equals("Run created."))
                                finish();
                        }
                    })
                    .show();
    }

    @Override
    public void onCreateRunFail(String message) {
        loadingScreen.hide();
        dialogFactory.setTitle("Run creation failed")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Okay", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;

        if(requestCode == MAP_CODE){
            ArrayList<PositionDTO> positionDTOS = (ArrayList<PositionDTO>)data.getSerializableExtra("path");
            individualNewRunPresenter.setPositionDTOS(positionDTOS);
        }
        else if(requestCode == DATE_CODE){
            Date date = (Date) data.getSerializableExtra("date");
            individualNewRunPresenter.setDate(date);
            TextView textView = findViewById(R.id.text_individual_new_run_date);
            textView.setText(date.toString());
        }
        else if(requestCode == TIME_CODE){
            Time time = (Time) data.getSerializableExtra("time");
            individualNewRunPresenter.setTime(time);
            TextView textView = findViewById(R.id.text_individual_new_run_time);
            textView.setText(time.toString());
        }
    }

    @Override //finish activity if back button is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
