package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.task.thirdParty.GroupRequestTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdParty.IndividualRequestTask;
import com.github.ferrantemattarutigliano.software.client.view.RequestView;

import java.sql.Date;

public class RequestPresenter extends Presenter<RequestView>{
    public RequestPresenter(RequestView view) {
        super(view);
    }

    public void doIndividualRequest(IndividualRequestDTO individualRequestDTO){
        new IndividualRequestTask(individualRequestDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRequestSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onRequestFail(message);
            }
        }).execute();
    }

    public void doGroupRequest(GroupRequestDTO groupRequestDTO){
        new GroupRequestTask(groupRequestDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRequestSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onRequestFail(message);
            }
        }).execute();
    }
}
