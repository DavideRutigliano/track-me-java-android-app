package com.github.ferrantemattarutigliano.software.client.view.individual;

import org.osmdroid.bonuspack.routing.Road;

public interface IndividualViewMapView {
    void updatePositionOnMap(String position);
    void drawRunPath(Road road);
}
