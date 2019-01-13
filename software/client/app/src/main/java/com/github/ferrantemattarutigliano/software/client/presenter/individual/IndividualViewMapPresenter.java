package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import android.content.Context;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualRoadTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualViewMapView;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.Iterator;

public class IndividualViewMapPresenter extends Presenter<IndividualViewMapView> {

    private MapView map;

    public IndividualViewMapPresenter(IndividualViewMapView view, MapView map) {
        super(view);
        this.map = map;
    }

    public void centerToGeoPoint(GeoPoint geoPoint){
        IMapController mapController = map.getController();
        mapController.setCenter(geoPoint);
        mapController.setZoom(18f);
    }

    public void addPosition(String athlete, PositionDTO position) {
        Iterator iterator = map.getOverlays().iterator();
        while (iterator.hasNext()) {
            Overlay overlay = (Overlay) iterator.next();
            if(overlay instanceof Marker) {
                Marker m = (Marker) overlay;
                if (m.getTitle().equals(athlete)) {
                    map.getOverlays().remove(m);
                }
            }
        }
        double lat = position.getLatitude();
        double lon = position.getLongitude();
        GeoPoint geoPoint = new GeoPoint(lat, lon);
        Marker marker = new Marker(map);
        marker.setPosition(geoPoint);
        marker.setTitle(athlete);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                marker.showInfoWindow();
                return true;
            }
        });
        map.getOverlays().add(marker);
    }

    public void calculateRoad(Context context, ArrayList<PositionDTO> runPath){
        ArrayList<GeoPoint> path = new ArrayList<>();
        for(PositionDTO p : runPath){
            GeoPoint g = new GeoPoint(p.getLatitude(), p.getLongitude());
            path.add(g);
        }
        new IndividualRoadTask(new OSRMRoadManager(context), path, new AsyncResponse<Road>() {
            @Override
            public void taskFinish(Road output) {
                view.drawRunPath(output);
            }

            @Override
            public void taskFailMessage(String message) {
            }
        }).execute();
    }
}
