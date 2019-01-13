package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualChangeProfileTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdparty.ThirdPartyChangeProfileTask;
import com.github.ferrantemattarutigliano.software.client.view.ChangeAccountDataView;

public class ChangeAccountDataPresenter extends Presenter<ChangeAccountDataView> {
    private String success = "Profile updated.";

    public ChangeAccountDataPresenter(ChangeAccountDataView view) {
        super(view);
    }

    public void doChangeIndividualAccountData(IndividualDTO individualDTO){
        new IndividualChangeProfileTask(SessionDirector.USERNAME, individualDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                if(output.equals(success))
                    view.onProfileUpdateSuccess(output);
                else
                    view.onProfileUpdateFail(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onProfileUpdateFail(message);
            }
        }).execute();

    }

    public void doChangeThirdPartyAccountData(ThirdPartyDTO thirdPartyDTO){
        new ThirdPartyChangeProfileTask(SessionDirector.USERNAME, thirdPartyDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                if(output.equals(success))
                    view.onProfileUpdateSuccess(output);
                else
                    view.onProfileUpdateFail(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onProfileUpdateFail(message);
            }
        }).execute();
    }
}
