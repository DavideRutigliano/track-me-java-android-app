package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.ferrantemattarutigliano.software.client.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class IndividualMapActivity extends AppCompatActivity {
    private MapView map;
    private ArrayList<Marker> markers;
    private boolean deleteMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_individual_map);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        markers = new ArrayList<>();
        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
        mapController.setZoom(13.5);
        addMapEventsOverlay();
    }

    private void addMapEventsOverlay(){
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                for(Marker m : markers){
                    m.closeInfoWindow();
                }
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                Marker marker = new Marker(map);
                marker.setPosition(p);
                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        marker.showInfoWindow();
                        if(deleteMarker){
                            markers.remove(marker);
                            new RoadTask().execute();
                        }
                        return true;
                    }
                });
                markers.add(marker);
                new RoadTask().execute();
                return true;
            }
        };

        MapEventsOverlay mapEventsOverlay  = new MapEventsOverlay(getBaseContext(), mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);
        FloatingActionButton button = findViewById(R.id.button_individual_map_delete_marker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMarker = !deleteMarker;
            }
        });
    }

    private void draw(Road road){
        map.getOverlays().clear();
        addMapEventsOverlay();
        rewriteMarkers();
        if(road != null){
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);
        }
        map.invalidate();
    }

    private void rewriteMarkers(){
        map.getOverlays().addAll(markers);
        if(!markers.isEmpty())
            markers.get(0).setTitle("Start");
        if(markers.size() > 1)
            markers.get(markers.size()-1).setTitle("End");
        if(markers.size() < 2)
            return;
        for(int i = 1; i < markers.size()-1; ++i){
            markers.get(i).setTitle("Waypoint " + i);
        }
    }

    private class RoadTask extends AsyncTask<Void, Void, Road>{
        @Override
        protected Road doInBackground(Void... voids) {
            RoadManager roadManager = new OSRMRoadManager(getApplicationContext());
            ArrayList<GeoPoint> waypoints = new ArrayList<>();
            for(Marker m : markers){
                waypoints.add(m.getPosition());
            }
            Road road;
            try {
                if(waypoints.isEmpty()) return null;
                road = roadManager.getRoad(waypoints);
            }catch (Exception e){
                Log.e("road_error", e.getMessage());
                throw new RuntimeException();
            }
            return road;
        }

        @Override
        protected void onPostExecute(Road road) {
            draw(road);
        }
    }
}
