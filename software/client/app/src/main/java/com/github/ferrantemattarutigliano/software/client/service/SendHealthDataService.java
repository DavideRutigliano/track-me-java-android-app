package com.github.ferrantemattarutigliano.software.client.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualInsertDataTask;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.util.Collections;

public class SendHealthDataService extends Service implements DataClient.OnDataChangedListener {

    private static final String datapath = "/data_path";
    private static boolean isDeviceConnected = false;

    public SendHealthDataService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Wearable.getDataClient(getApplicationContext()).addListener(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Wearable.getDataClient(getApplicationContext()).removeListener(this);
        isDeviceConnected = false;
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        isDeviceConnected = true;
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (datapath.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String message = dataMapItem.getDataMap().getString("message");
                    doInsertData(message);
                }
            }
        }
    }

    private void doInsertData(String message) {
        String name = StringUtils.substringBefore(message, ":");
        String value = StringUtils.substringAfter(message, ":");
        HealthDataDTO healthDataDTO = new HealthDataDTO();
        healthDataDTO.setName(name);
        healthDataDTO.setValue(value);
        java.util.Date date = new java.util.Date();
        healthDataDTO.setDate(new java.sql.Date(date.getTime()));
        healthDataDTO.setTime(new Time(date.getTime()));

        new IndividualInsertDataTask(Collections.singleton(healthDataDTO), new AsyncResponse<String>() {
            @Override
            public void taskFinish(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void taskFailMessage(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    public static boolean isDeviceConnected() {
        return isDeviceConnected;
    }
}
