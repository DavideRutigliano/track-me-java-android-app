package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualGetProfileTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualHomeView;

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
