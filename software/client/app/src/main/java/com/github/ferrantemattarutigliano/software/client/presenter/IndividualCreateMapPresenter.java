package com.github.ferrantemattarutigliano.software.client.presenter;

import android.content.Context;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.RoadTask;
import com.github.ferrantemattarutigliano.software.client.view.IndividualCreateMapView;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Collection;

public class IndividualCreateMapPresenter extends Presenter<IndividualCreateMapView> {
    private ArrayList<Marker> markers;
    private boolean isDeletingMarker;
    private MapView map;

    public IndividualCreateMapPresenter(IndividualCreateMapView view, MapView map) {
        super(view);
        this.map = map;
        markers = new ArrayList<>();
        isDeletingMarker = false;
    }

    public void centerToGeoPoint(GeoPoint geoPoint){
        IMapController mapController = map.getController();
        mapController.setCenter(geoPoint);
        mapController.setZoom(16.5);
    }

    public void manageActionMarker(final Context context, GeoPoint p){
        Marker marker = new Marker(map);
        marker.setPosition(p);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                marker.showInfoWindow();
                if (isDeletingMarker) {
                    marker.closeInfoWindow();
                    markers.remove(marker);
                    recalculateRoad(context);
                }
                return true;
            }
        });
        markers.add(marker);
        recalculateRoad(context);
    }

    public void recalculateRoad(Context context){
        new RoadTask(new OSRMRoadManager(context), markers, new AsyncResponse<Road>() {
            @Override
            public void taskFinish(Road output) {
                view.clearMap();
                rewriteMarkers();
                view.drawRunPath(output);
            }

            @Override
            public void taskFailMessage(String message) {
            }
        }).execute();
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

    public void closeAllInformationWindow(){
        for(Marker m : markers){
            m.closeInfoWindow();
        }
    }

    public void clearAllMarkers(Context context){
        markers.clear();
        recalculateRoad(context);
    }

    public boolean isDeletingMarker() {
        return isDeletingMarker;
    }

    public void invertDeletingMarker(){
        isDeletingMarker = !isDeletingMarker;
    }

    public ArrayList<PositionDTO> convertMarkersToPositions(){
        ArrayList<PositionDTO> positionDTOS = new ArrayList<>();
        for(Marker m : markers){
            PositionDTO position = new PositionDTO();
            GeoPoint markerPosition = m.getPosition();
            position.setLatitude(markerPosition.getLatitude());
            position.setLongitude(markerPosition.getLongitude());
            positionDTOS.add(position);
        }
        return positionDTOS;
    }
}
