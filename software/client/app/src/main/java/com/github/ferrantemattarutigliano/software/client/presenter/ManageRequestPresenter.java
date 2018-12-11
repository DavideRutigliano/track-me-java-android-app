package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualGetRequestsTask;
import com.github.ferrantemattarutigliano.software.client.view.ManageRequestView;

import java.util.Collection;

public class ManageRequestPresenter extends Presenter<ManageRequestView>{

    public ManageRequestPresenter(ManageRequestView view) {
        super(view);
    }

    public Collection<IndividualRequestDTO> getRequests(){
        //todo add real individual id
        new IndividualGetRequestsTask(1L, new AsyncResponse<Collection<IndividualRequestDTO>>() {
            @Override
            public void taskFinish(Collection<IndividualRequestDTO> output) {
                if(output.isEmpty()){
                    view.noRequestReceived();
                    return;
                }
                view.onShowRequests(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onShowRequestsFail(message);
            }
        });
        return null;
    }
}
