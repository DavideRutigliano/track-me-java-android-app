package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.jjoe64.graphview.GraphView;

import java.util.Collection;

public class ThirdPartyHealthDataActivity extends AppCompatActivity {
    private GraphView graphView;
    private Collection<HealthDataDTO> healthData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_health_data);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        graphView = findViewById(R.id.graph_third_party_healthdata);

        Bundle bundle = getIntent().getExtras();
        try{
            healthData = (Collection<HealthDataDTO>) bundle.getSerializable("healthdata");
        }
        catch (RuntimeException e){
            Toast.makeText(this, "Failed to get healthdata bundle", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
