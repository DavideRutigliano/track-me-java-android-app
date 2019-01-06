package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualCreatedRunsPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualCreatedRunsView;

import java.util.Collection;

public class IndividualCreatedRunsActivity extends AppCompatActivity implements IndividualCreatedRunsView {
    private IndividualCreatedRunsPresenter individualCreatedRunsPresenter;
    private LoadingScreen loadingScreen;
    private AlertDialog.Builder dialogFactory;
    private final int BUTTON_WIDTH = 90;
    private final int BUTTON_HEIGHT = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_created_runs);
        if(getSupportActionBar() != null)
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

    @Override
    protected void onResume() {
        super.onResume();
        loadingScreen.show();
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
        container.removeAllViews();
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
            //add vertical spacing
            addSpace(container, true);
            //create title
            TextView titleView = new TextView(getApplicationContext());
            titleView.setText(runDTO.getTitle());
            titleView.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(titleView);
            //create buttons
            addSpace(linearLayout);
            createDeleteButton(linearLayout, runDTO);
            addSpace(linearLayout);
            createInfoButton(linearLayout, runDTO);
            addSpace(linearLayout);
            createModifyButton(linearLayout);
            addSpace(linearLayout);
            if(runDTO.getState().equals("created"))
                createStartButton(linearLayout, runDTO);

            container.addView(linearLayout);
        }
        loadingScreen.hide();
    }

    @Override
    public void onStartRun(String message) {
        loadingScreen.hide();
        dialogFactory.setTitle("Run Started")
                .setMessage(message)
                .setPositiveButton("Okay", null)
                .setNegativeButton(null, null)
                .show();
        individualCreatedRunsPresenter.doFetchRun();
    }

    @Override
    public void onDeleteRun(String message) {
        loadingScreen.hide();
        dialogFactory.setTitle("Run Deleted")
                .setMessage(message)
                .setPositiveButton("Okay", null)
                .setNegativeButton(null, null)
                .show();
        individualCreatedRunsPresenter.doFetchRun();
    }

    //todo finish the functionality of buttons
    private void createDeleteButton(ViewGroup layout, final RunDTO runDTO) {
        ImageButton deleteButton = new ImageButton(getApplicationContext());
        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Delete Run")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                individualCreatedRunsPresenter.doDeleteRun(runDTO.getId());
                                loadingScreen.show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        layout.addView(deleteButton);
    }

    private void createInfoButton(ViewGroup layout, final RunDTO runDTO) {
        ImageButton infoButton = new ImageButton(getApplicationContext());
        infoButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_info_details));
        infoButton.setLayoutParams(new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT));
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Run Information")
                        .setMessage(runDTO.toString())
                        .setPositiveButton("Okay", null)
                        .setNegativeButton(null, null)
                        .show();
            }
        });
        layout.addView(infoButton);
    }

    private void createModifyButton(ViewGroup layout) {
        ImageButton modifyButton = new ImageButton(getApplicationContext());
        modifyButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
        modifyButton.setLayoutParams(new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT));
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        layout.addView(modifyButton);
    }

    private void createStartButton(ViewGroup layout, final RunDTO runDTO) {
        ImageButton startButton = new ImageButton(getApplicationContext());
        startButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        startButton.setLayoutParams(new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Start Run")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                individualCreatedRunsPresenter.doStartRun(runDTO.getId());
                                loadingScreen.show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        layout.addView(startButton);
    }

    private void addSpace(ViewGroup layout){
        addSpace(layout, false);
    }

    private void addSpace(ViewGroup layout, boolean isVertical){
        int width = 0;
        int height = 0;

        if(isVertical) height = 70;
        else width = 25;

        Space space = new Space(getApplicationContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        layout.addView(space);
    }
}
