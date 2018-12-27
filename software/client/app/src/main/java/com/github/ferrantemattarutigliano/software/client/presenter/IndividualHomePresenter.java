package com.github.ferrantemattarutigliano.software.client.presenter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualGetProfileTask;
import com.github.ferrantemattarutigliano.software.client.view.IndividualHomeView;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompCallback;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompClient;
import com.github.ferrantemattarutigliano.software.client.websocket.payload.StompFrame;

public class IndividualHomePresenter extends Presenter<IndividualHomeView> {
    public IndividualHomePresenter(IndividualHomeView view) {
        super(view);
    }

    public void doFetchProfile(){
        new IndividualGetProfileTask(SessionDirector.USERNAME, new AsyncResponse<IndividualDTO>() {
            @Override
            public void taskFinish(IndividualDTO output) {
                view.notifyUser();
                view.onProfileFetch(output);
            }

            @Override
            public void taskFailMessage(String message) {
                Log.e("FETCH_PROFILE_ERROR", message);
            }
        }).execute();
    }
}
