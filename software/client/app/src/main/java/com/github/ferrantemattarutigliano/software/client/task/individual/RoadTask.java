package com.github.ferrantemattarutigliano.software.client.task.individual;

import android.os.AsyncTask;
import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class RoadTask extends AsyncTask<Void, Void, Road> {
    private RoadManager roadManager;
    private ArrayList<Marker> markers;
    private AsyncResponse<Road> asyncResponse;

    public RoadTask(RoadManager roadManager, ArrayList<Marker> markers, AsyncResponse<Road> asyncResponse) {
        this.roadManager = roadManager;
        this.markers = markers;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected Road doInBackground(Void... voids) {
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
        asyncResponse.taskFinish(road);
    }
}
