package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualViewMapPresenter;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualViewMapView;

import org.apache.commons.lang3.StringUtils;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collection;

public class IndividualViewMapActivity extends AppCompatActivity
        implements IndividualViewMapView {
    private MapView map;
    private IndividualViewMapPresenter individualViewMapPresenter;
    private ArrayList<PositionDTO> path;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("athletes");
            updatePositionOnMap(content);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_individual_view_map);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        individualViewMapPresenter = new IndividualViewMapPresenter(this, map);
        setMapPositionToCurrentLocation();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("RefreshMap"));
        Bundle bundle = getIntent().getExtras();
        path = (ArrayList<PositionDTO>) bundle.getSerializable("path");
        individualViewMapPresenter.calculateRoad(this, path);
    }

    public void drawRunPath(Road road) {
        if (road != null) {
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);
            map.invalidate();
        }
    }

    @Override
    public void updatePositionOnMap(String position) {
        String athlete = StringUtils.substringBetween(position, "Athlete: ", ".");
        String latitude = StringUtils.substringBetween(position, "lat=", ",");
        String longitude = StringUtils.substringBetween(position, ", lon=", ";");
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setLongitude(Double.parseDouble(longitude));
        positionDTO.setLatitude(Double.parseDouble(latitude));
        individualViewMapPresenter.addPosition(athlete, positionDTO);
    }

    @Override //finish activity if back button is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMapPositionToCurrentLocation() {
        //set the position to Milan. In the real application this would set the position
        //to the current position
        GeoPoint geoPoint = new GeoPoint(45.4642, 9.1900);
        individualViewMapPresenter.centerToGeoPoint(geoPoint);
    }

}
