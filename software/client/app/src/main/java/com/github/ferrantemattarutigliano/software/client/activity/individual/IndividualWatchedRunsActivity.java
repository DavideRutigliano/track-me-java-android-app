package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualWatchedRunsPresenter;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.util.Constant;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualWatchedRunsView;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;

public class IndividualWatchedRunsActivity extends AppCompatActivity implements IndividualWatchedRunsView {
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;
    private IndividualWatchedRunsPresenter individualWatchedRunsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_watched_runs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        individualWatchedRunsPresenter = new IndividualWatchedRunsPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);

        ViewGroup layout = findViewById(R.id.layout_individual_watched_runs);
        loadingScreen = new LoadingScreen(layout, "Getting data...");
        loadingScreen.show();
        individualWatchedRunsPresenter.doFetchRun();
    }

    @Override //finish activity if back button is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void noWatchedRuns() {
        ViewGroup container = findViewById(R.id.container_watched_runs);
        container.removeAllViews();
        TextView textView = new TextView(getApplicationContext());
        CharSequence text = "No run watched yet!";
        textView.setText(text);
        container.addView(textView);
        loadingScreen.hide();
    }

    @Override
    public void onRunFetch(Collection<RunDTO> output) {
        ViewGroup container = findViewById(R.id.container_watched_runs);
        container.removeAllViews();
        for (final RunDTO runDTO : output) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //create title
            TextView titleView = new TextView(getApplicationContext());
            titleView.setLayoutParams(new LinearLayout.LayoutParams(Constant.RUN_TITLE_WIDTH,ViewGroup.LayoutParams.WRAP_CONTENT));
            titleView.setText(runDTO.getTitle());
            linearLayout.addView(titleView);
            //create buttons
            final Long runId = runDTO.getId();
            Button watchButton = new Button(getApplicationContext());
            CharSequence watchText = "Watch";
            final Date runDate = runDTO.getDate();
            final Time runTime = runDTO.getTime();
            watchButton.setText(watchText);
            watchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    java.util.Date date = new java.util.Date();
                    Date currentDate = new Date(date.getTime());
                    Time currentTime = new Time(date.getTime());
                    boolean isRunStarted = runDTO.getState().equals("started")
                                            || runDate.before(currentDate)
                                            || (runDate.equals(currentDate) && runTime.before(currentTime));
                    if(!isRunStarted){
                        dialogFactory.setTitle("Run not started")
                                .setMessage("This run is not started yet. Wait until:" +
                                        "\n" + runDate + "\n" + runTime)
                                .setPositiveButton("Okay", null)
                                .show();
                    }
                    else {
                        loadingScreen.show();
                        Intent intent = new Intent(getApplicationContext(), IndividualViewMapActivity.class);
                        startActivity(intent);
                    }
                }
            });
            Button unwatchButton = new Button(getApplicationContext());
            CharSequence unwatchText = "Unwatch";
            unwatchButton.setText(unwatchText);
            unwatchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingScreen.show();
                    String topic = "/run/" + runId + "/" + SessionDirector.USERNAME;
                    SessionDirector.getStompClient().unsubscribe(topic);
                    SharedPreferences sharedPreferences = getSharedPreferences("sub_" + SessionDirector.USERNAME, MODE_PRIVATE);
                    SessionDirector.removeTopicSubscription(topic, sharedPreferences);
                    individualWatchedRunsPresenter.unwatchRun(runId);
                }
            });
            linearLayout.addView(watchButton);
            linearLayout.addView(unwatchButton);
            container.addView(linearLayout);
        }
        loadingScreen.hide();
    }

    @Override
    public void onRunUnwatch(String message) {
        dialogFactory.setTitle("Run unwatched")
                    .setMessage(message)
                    .setPositiveButton("Okay", null)
                    .show();
        individualWatchedRunsPresenter.doFetchRun();
    }
}
