package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualShowRunsTask;
import com.github.ferrantemattarutigliano.software.client.view.IndividualCreatedRunsView;

import java.util.Collection;

public class IndividualCreatedRunsPresenter extends Presenter<IndividualCreatedRunsView> {
    public IndividualCreatedRunsPresenter(IndividualCreatedRunsView view) {
        super(view);
    }

    public void doFetchRun(){
        new IndividualShowRunsTask("created", new AsyncResponse<Collection<RunDTO>>() {
            @Override
            public void taskFinish(Collection<RunDTO> output) {
                if(output.isEmpty()){
                    view.noCreatedRuns();
                    return;
                }
                view.onRunFetch(output);
            }

            @Override
            public void taskFailMessage(String message) {
                //todo finish with timeout...
            }
        }).execute();
    }
}
