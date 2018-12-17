package com.github.ferrantemattarutigliano.software.client.view;

import org.osmdroid.bonuspack.routing.Road;

public interface IndividualCreateMapView {
    void drawRunPath(Road road);
    void clearMap();
}
