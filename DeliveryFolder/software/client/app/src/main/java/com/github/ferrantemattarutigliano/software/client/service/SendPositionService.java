package com.github.ferrantemattarutigliano.software.client.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualSendPositionTask;
import com.google.android.gms.wearable.Wearable;

public class SendPositionService extends Service {

    private static final long FIVE_SECONDS = 5000L;

    final Handler handler = new Handler();

    public SendPositionService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startLocationMonitoring();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationMonitoring() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                LocationManager locationManager;
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        PositionDTO positionDTO = new PositionDTO();
                        positionDTO.setLatitude(location.getLatitude());
                        positionDTO.setLongitude(location.getLongitude());
                        IndividualSendPositionTask individualSendPositionTask = new IndividualSendPositionTask(positionDTO,
                                new AsyncResponse<String>() {
                                    @Override
                                    public void taskFinish(String message) {
                                    }

                                    @Override
                                    public void taskFailMessage(String message) {
                                    }
                                });
                        individualSendPositionTask.execute();
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
                };

                locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        FIVE_SECONDS, 0, locationListener);
            }
        });
    }
}