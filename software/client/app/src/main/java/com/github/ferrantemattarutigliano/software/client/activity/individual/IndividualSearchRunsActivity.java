package com.github.ferrantemattarutigliano.software.client.activity.individual;

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
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualSearchRunsPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualSearchRunsView;

import java.util.Collection;

public class IndividualSearchRunsActivity extends AppCompatActivity implements IndividualSearchRunsView {
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;
    private IndividualSearchRunsPresenter individualSearchRunsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_search_runs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        individualSearchRunsPresenter = new IndividualSearchRunsPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);

        ViewGroup layout = findViewById(R.id.layout_individual_search_runs);
        loadingScreen = new LoadingScreen(layout, "Getting data...");
        loadingScreen.show();
        individualSearchRunsPresenter.doFetchRun();
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
    public void noAvailableRuns() {
        ViewGroup container = findViewById(R.id.container_search_runs);
        container.removeAllViews();
        TextView textView = new TextView(getApplicationContext());
        CharSequence text = "No run available yet!";
        textView.setText(text);
        container.addView(textView);
        loadingScreen.hide();
    }

    @Override
    public void onRunFetch(Collection<RunDTO> output) {
        ViewGroup container = findViewById(R.id.container_search_runs);
        container.removeAllViews();
        for (RunDTO runDTO : output) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //create title
            TextView titleView = new TextView(getApplicationContext());
            titleView.setText(runDTO.getTitle());
            linearLayout.addView(titleView);
            //create buttons
            final Long runId = runDTO.getId();
            Button watchButton = new Button(getApplicationContext());
            CharSequence watchText = "Watch";
            watchButton.setText(watchText);
            watchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingScreen.show();
                    individualSearchRunsPresenter.watchRun(runId);
                }
            });
            Button unwatchButton = new Button(getApplicationContext());
            CharSequence unwatchText = "Enroll";
            unwatchButton.setText(unwatchText);
            unwatchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingScreen.show();
                    individualSearchRunsPresenter.enrollRun(runId);
                }
            });
            linearLayout.addView(watchButton);
            linearLayout.addView(unwatchButton);
            container.addView(linearLayout);
        }
        loadingScreen.hide();
    }

    @Override
    public void onRunInteraction(String message) {
        dialogFactory.setTitle("Information")
                    .setMessage(message)
                    .setPositiveButton("Okay", null)
                    .show();
        individualSearchRunsPresenter.doFetchRun();
    }
}
