package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualGetRequestsTask;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualHandleRequest;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualManageRequestView;

import java.util.Collection;

public class IndividualManageRequestPresenter extends Presenter<IndividualManageRequestView> {

    public IndividualManageRequestPresenter(IndividualManageRequestView view) {
        super(view);
    }

    public void getRequests(){
        new IndividualGetRequestsTask(SessionDirector.USERNAME, new AsyncResponse<Collection<ReceivedRequestDTO>>() {
            @Override
            public void taskFinish(Collection<ReceivedRequestDTO> output) {
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
        }).execute();
    }

    public void handleRequest(ReceivedRequestDTO receivedRequestDTO){
        new IndividualHandleRequest(SessionDirector.USERNAME, receivedRequestDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRequestHandleSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onRequestHandleFail(message);
            }
        }).execute();
    }
}
