package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.sql.Date;
import java.util.ArrayList;
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
            Toast.makeText(this, "Failed to get health data bundle", Toast.LENGTH_LONG)
                    .show();
            finish();
            return;
        }
        buildGraph();
    }

    private void buildGraph(){
        if(healthData == null || healthData.isEmpty()){
            Toast.makeText(this, "No health data available", Toast.LENGTH_LONG)
                    .show();
            finish();
            return;
        }

        ArrayList<DataPoint> dataPointArrayList = new ArrayList<>();

        for(HealthDataDTO h : healthData){
            double value = Double.parseDouble(h.getValue());
            DataPoint d = new DataPoint(h.getDate(), value);
            dataPointArrayList.add(d);
        }

        DataPoint[] dataPoints = (DataPoint[]) dataPointArrayList.toArray();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graphView.addSeries(series);
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
