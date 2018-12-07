package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.task.thirdParty.GroupRequestTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdParty.IndividualRequestTask;
import com.github.ferrantemattarutigliano.software.client.view.RequestView;

public class RequestPresenter {
    private RequestView requestView;

    public RequestPresenter(RequestView requestView) {
        this.requestView = requestView;
    }

    public void doIndividualRequest(IndividualRequestDTO individualRequestDTO){
        new IndividualRequestTask(individualRequestDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                requestView.onRequestSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                requestView.onRequestFail(message);
            }
        }).execute();
    }

    public void doGroupRequest(GroupRequestDTO groupRequestDTO){
        new GroupRequestTask(groupRequestDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                requestView.onRequestSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                requestView.onRequestFail(message);
            }
        }).execute();
    }
}
