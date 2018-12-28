package com.github.ferrantemattarutigliano.software.client.presenter.thirdparty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.thirdparty.ThirdPartyGroupRequestTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdparty.ThirdPartyIndividualRequestTask;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyRequestView;

public class ThirdPartyRequestPresenter extends Presenter<ThirdPartyRequestView> {
    public ThirdPartyRequestPresenter(ThirdPartyRequestView view) {
        super(view);
    }

    public void doIndividualRequest(IndividualRequestDTO individualRequestDTO){
        new ThirdPartyIndividualRequestTask(individualRequestDTO, new AsyncResponse<String>() {
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
        new ThirdPartyGroupRequestTask(groupRequestDTO, new AsyncResponse<String>() {
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
