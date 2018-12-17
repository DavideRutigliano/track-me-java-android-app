package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.IndividualNewRunPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.IndividualNewRunView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class IndividualNewRunActivity extends AppCompatActivity implements IndividualNewRunView {
    private final int MAP_CODE = 100;
    private IndividualNewRunPresenter individualNewRunPresenter;
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_new_run);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        individualNewRunPresenter = new IndividualNewRunPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);
        ViewGroup layout = findViewById(R.id.layout_individual_new_run);
        loadingScreen = new LoadingScreen(layout);
        loadingScreen.changeMessage("Sending...");
        ImageButton mapButton = findViewById(R.id.image_button_individual_new_run_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IndividualCreateMapActivity.class);
                startActivityForResult(intent, MAP_CODE);
            }
        });
        final TextView titleText = findViewById(R.id.text_individual_new_run_title);
        final TextView dateText = findViewById(R.id.text_individual_new_run_date);
        final TextView timeText = findViewById(R.id.text_individual_new_run_time);
        Button confirmButton = findViewById(R.id.button_individual_new_run_create);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingScreen.show();
                String title = titleText.getText().toString();
                Date date = null;
                Time time = null; //todo find a way to get date and time
                individualNewRunPresenter.doCreateRun(title, date, time);
            }
        });
    }

    @Override
    public void pathTooShort() {
        onCreateRunFail("Run path is too short. Add at least 2 way points.");
    }

    @Override
    public void onCreateRunSuccess(String message) {
        loadingScreen.hide();
        dialogFactory.setTitle("Information")
                    .setMessage(message)
                    .setPositiveButton("Okay", null)
                    .show();
    }

    @Override
    public void onCreateRunFail(String message) {
        loadingScreen.hide();
        dialogFactory.setTitle("Run creation failed")
                .setMessage(message)
                .setPositiveButton("Okay", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAP_CODE && resultCode == RESULT_OK){
            ArrayList<PositionDTO> positionDTOS = (ArrayList<PositionDTO>)data.getSerializableExtra("positions");
            individualNewRunPresenter.setPositionDTOS(positionDTOS);
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
