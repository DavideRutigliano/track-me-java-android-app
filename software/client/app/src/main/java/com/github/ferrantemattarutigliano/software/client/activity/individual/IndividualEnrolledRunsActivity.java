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
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualEnrolledRunsPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualEnrolledRunsView;

import java.util.Collection;

public class IndividualEnrolledRunsActivity extends AppCompatActivity implements IndividualEnrolledRunsView {
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;
    IndividualEnrolledRunsPresenter individualEnrolledRunsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_enrolled_runs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        individualEnrolledRunsPresenter = new IndividualEnrolledRunsPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);

        ViewGroup layout = findViewById(R.id.layout_individual_enrolled_runs);
        loadingScreen = new LoadingScreen(layout, "Getting data...");
        loadingScreen.show();
        individualEnrolledRunsPresenter.doFetchRun();
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
    public void noEnrolledRuns() {
        ViewGroup container = findViewById(R.id.container_enrolled_runs);
        TextView textView = new TextView(getApplicationContext());
        CharSequence text = "No run enrolled yet!";
        textView.setText(text);
        container.addView(textView);
        loadingScreen.hide();
    }

    @Override
    public void onRunFetch(Collection<RunDTO> output) {
        ViewGroup container = findViewById(R.id.container_enrolled_runs);
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
            Button unrollButton = new Button(getApplicationContext());
            unrollButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    individualEnrolledRunsPresenter.unrollRun(runId);
                    loadingScreen.show();
                }
            });
            linearLayout.addView(unrollButton);

            container.addView(linearLayout);
        }
        loadingScreen.hide();
    }

    @Override
    public void onRunUnroll(String message) {
        dialogFactory.setTitle("Run unrolled")
                .setMessage(message)
                .setPositiveButton("Okay", null)
                .show();
        individualEnrolledRunsPresenter.doFetchRun();
    }
}
