package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.IndividualCreatedRunsPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.IndividualCreatedRunsView;

import java.util.Collection;

public class IndividualCreatedRunsActivity extends AppCompatActivity implements IndividualCreatedRunsView {
    private IndividualCreatedRunsPresenter individualCreatedRunsPresenter;
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_created_runs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        individualCreatedRunsPresenter = new IndividualCreatedRunsPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);

        ViewGroup layout = findViewById(R.id.layout_individual_created_runs);
        loadingScreen = new LoadingScreen(layout, "Getting data...");
        loadingScreen.show();
        FloatingActionButton createRunButton = findViewById(R.id.floating_button_created_runs_create);
        createRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IndividualNewRunActivity.class);
                startActivity(intent);
            }
        });
        individualCreatedRunsPresenter.doFetchRun();
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
    public void noCreatedRuns() {
        ViewGroup container = findViewById(R.id.container_created_runs);
        TextView textView = new TextView(getApplicationContext());
        CharSequence text = "No run created yet!";
        textView.setText(text);
        container.addView(textView);
        loadingScreen.hide();
    }

    @Override
    public void onRunFetch(Collection<RunDTO> output) {
        ViewGroup container = findViewById(R.id.container_created_runs);
        container.removeAllViews();
        for (RunDTO runDTO : output) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //create title
            TextView titleView = new TextView(getApplicationContext());
            titleView.setText(runDTO.getTitle());
            linearLayout.addView(titleView);
            //create buttons
            createDeleteButton(linearLayout);
            createInfoButton(linearLayout);
            createModifyButton(linearLayout);
            createStartButton(linearLayout);

            container.addView(linearLayout);
        }
        loadingScreen.hide();
    }

    private void createDeleteButton(ViewGroup layout) {
        ImageButton deleteButton = new ImageButton(getApplicationContext());
        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Delete Run")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        layout.addView(deleteButton);
    }

    private void createInfoButton(ViewGroup layout) {
        ImageButton infoButton = new ImageButton(getApplicationContext());
        infoButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_info_details));
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        layout.addView(infoButton);
    }

    private void createModifyButton(ViewGroup layout) {
        ImageButton modifyButton = new ImageButton(getApplicationContext());
        modifyButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        layout.addView(modifyButton);
    }

    private void createStartButton(ViewGroup layout) {
        ImageButton startButton = new ImageButton(getApplicationContext());
        startButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Start Run")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        layout.addView(startButton);
    }
}
