package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.IndividualCreateMapPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LocationProvider;
import com.github.ferrantemattarutigliano.software.client.view.IndividualCreateMapView;

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
    private LocationManager locationManager;
    private AlertDialog.Builder dialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_individual_create_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        individualCreateMapPresenter = new IndividualCreateMapPresenter(this, map);
        dialogFactory = new AlertDialog.Builder(this);
        setupLateralButtons();
        addMapEventsOverlay();
        initLocationService();
        setMapPositionToCurrentLocation();
    }

    private void initLocationService() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //if position permission wasn't granted, ask for it
            ActivityCompat.requestPermissions(IndividualCreateMapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, new LocationProvider());
            showInfoToast();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != 200) return;
        //permission granted
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initLocationService();
        }
        //permission not granted
        else{
            dialogFactory.setTitle("Location permission is required")
                    .setCancelable(false)
                    .setMessage("Without this permission it's not possible to use this feature. Do you want to " +
                            "give the permission?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initLocationService();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void setMapPositionToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //GeoPoint geoPoint = new GeoPoint(currentLocation);
            //individualCreateMapPresenter.centerToGeoPoint(geoPoint);
            //todo fix this
        }
    }

    private void showInfoToast() {
        Toast.makeText(getApplicationContext(), "Long press to add a waypoint to the map", Toast.LENGTH_LONG).show();
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
                                intent.putExtra("positions", positionDTOS);
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
