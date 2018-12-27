package com.github.ferrantemattarutigliano.software.client.presenter.thirdparty;

import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.task.thirdParty.ThirdPartyGetProfileTask;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyHomeView;

public class ThirdPartyHomePresenter extends Presenter<ThirdPartyHomeView> {
    public ThirdPartyHomePresenter(ThirdPartyHomeView view) {
        super(view);
    }

    public void doFetchProfile(){
        new ThirdPartyGetProfileTask(SessionDirector.USERNAME, new AsyncResponse<ThirdPartyDTO>() {
            @Override
            public void taskFinish(ThirdPartyDTO output) {
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
