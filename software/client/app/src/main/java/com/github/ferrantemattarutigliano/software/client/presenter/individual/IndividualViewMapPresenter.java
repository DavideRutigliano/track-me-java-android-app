package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualViewMapView;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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
            Marker m = (Marker) iterator.next();
            if (m.getTitle().equals(athlete)) {
                map.getOverlays().remove(m);
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
}
