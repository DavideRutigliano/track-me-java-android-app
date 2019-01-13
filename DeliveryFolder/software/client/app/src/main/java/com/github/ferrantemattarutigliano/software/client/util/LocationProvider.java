package com.github.ferrantemattarutigliano.software.client.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;

public class LocationProvider implements LocationListener {
    private PositionDTO positionDTO;

    public LocationProvider() {
        positionDTO = new PositionDTO();
    }

    @Override
    public void onLocationChanged(Location location) {
        positionDTO.setLatitude(location.getLatitude());
        positionDTO.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public PositionDTO getPositionDTO() {
        return positionDTO;
    }
}
