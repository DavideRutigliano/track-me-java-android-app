package com.github.ferrantemattarutigliano.software.client.task.individual;

import android.os.AsyncTask;
import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class IndividualRoadTask extends AsyncTask<Void, Void, Road> {
    private RoadManager roadManager;
    private ArrayList<GeoPoint> path;
    private AsyncResponse<Road> asyncResponse;

    public IndividualRoadTask(RoadManager roadManager, ArrayList<GeoPoint> path, AsyncResponse<Road> asyncResponse) {
        this.roadManager = roadManager;
        this.path = path;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected Road doInBackground(Void... voids) {
        Road road;
        try {
            if(path.isEmpty()) return null;
            road = roadManager.getRoad(path);
        }catch (RuntimeException e){
            Log.e("road_error", e.getMessage());
            return null;
        }
        return road;
    }

    @Override
    protected void onPostExecute(Road road) {
        asyncResponse.taskFinish(road);
    }
}
