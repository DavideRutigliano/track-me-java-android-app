package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualCreateMapPresenter;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualCreateMapView;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class IndividualCreateMapActivity extends AppCompatActivity
        implements IndividualCreateMapView {
    private MapView map;
    private IndividualCreateMapPresenter individualCreateMapPresenter;
    private AlertDialog.Builder dialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_individual_create_map);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        individualCreateMapPresenter = new IndividualCreateMapPresenter(this, map);
        dialogFactory = new AlertDialog.Builder(this);
        setupLateralButtons();
        addMapEventsOverlay();
        setMapPositionToCurrentLocation();
        reloadMap();
        showInfoToast(individualCreateMapPresenter.isDeletingMarker());
    }

    private void reloadMap(){
        Intent intent = getIntent();
        if(intent.hasExtra("path")){
            //recreate the map
            ArrayList<PositionDTO> path = (ArrayList)intent.getSerializableExtra("path");
            individualCreateMapPresenter.addPositions(this, path);
            //center to last marker inserted
            PositionDTO lastPosition = path.get(path.size() - 1);
            double lat = lastPosition.getLatitude();
            double lon = lastPosition.getLongitude();
            GeoPoint lastMarkerPosition = new GeoPoint(lat, lon);
            individualCreateMapPresenter.centerToGeoPoint(lastMarkerPosition);
        }
    }

    private void setMapPositionToCurrentLocation() {
        //set the position to Milan. In the real application this would set the position
        //to the current position
        GeoPoint geoPoint = new GeoPoint(45.4642, 9.1900);
        individualCreateMapPresenter.centerToGeoPoint(geoPoint);
    }

    private void showInfoToast(boolean isDeleting) {
        if(isDeleting){
            Toast.makeText(getApplicationContext(), "Press a marker to delete it", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Long press to add a waypoint to the map", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setupLateralButtons() {
        final FloatingActionButton markerButton = findViewById(R.id.button_individual_map_delete_marker);
        markerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                individualCreateMapPresenter.invertDeletingMarker();
                if (individualCreateMapPresenter.isDeletingMarker()) {
                    markerButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_input_add));
                } else {
                    markerButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                }
                showInfoToast(individualCreateMapPresenter.isDeletingMarker());
            }
        });

        final FloatingActionButton positionButton = findViewById(R.id.button_individual_map_position);
        positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapPositionToCurrentLocation();
            }
        });

        final FloatingActionButton deleteAllMarkersButton = findViewById(R.id.button_individual_map_delete_all_marker);
        deleteAllMarkersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Delete all markers")
                        .setMessage("Are you sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                individualCreateMapPresenter.clearAllMarkers(getApplicationContext());
                            }
                        })
                        .show();
            }
        });

        final FloatingActionButton completeButton = findViewById(R.id.button_individual_map_complete);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFactory.setTitle("Complete map")
                        .setMessage("Are you sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                ArrayList<PositionDTO> positionDTOS = individualCreateMapPresenter.convertMarkersToPositions();
                                intent.putExtra("path", positionDTOS);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    @Override //finish activity if back button is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMapEventsOverlay() {
        final MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                individualCreateMapPresenter.closeAllInformationWindow();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                individualCreateMapPresenter.manageActionMarker(getApplicationContext(), p);
                return true;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);
    }

    @Override
    public void drawRunPath(Road road) {
        if (road != null) {
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);
        }
        map.invalidate();
    }

    @Override
    public void clearMap() {
        map.getOverlays().clear();
        addMapEventsOverlay();
    }
}
