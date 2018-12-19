package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualModifyRunTask;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualShowRunsTask;
import com.github.ferrantemattarutigliano.software.client.view.IndividualEnrolledRunsView;
import com.github.ferrantemattarutigliano.software.client.view.IndividualSearchRunsView;

import java.util.Collection;

public class IndividualSearchRunsPresenter extends Presenter<IndividualSearchRunsView> {
    public IndividualSearchRunsPresenter(IndividualSearchRunsView view) {
        super(view);
    }

    public void doFetchRun(){
        new IndividualShowRunsTask("enrolled", new AsyncResponse<Collection<RunDTO>>() {
            @Override
            public void taskFinish(Collection<RunDTO> output) {
                if(output.isEmpty()){
                    view.noAvailableRuns();
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

    public void watchRun(Long runId){
        new IndividualModifyRunTask(runId, "watch", new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRunInteraction(output);
            }

            @Override
            public void taskFailMessage(String message) {
            }
        }).execute();
    }

    public void enrollRun(Long runId){
        new IndividualModifyRunTask(runId, "enroll", new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRunInteraction(output);
            }

            @Override
            public void taskFailMessage(String message) {
            }
        }).execute();
    }
}
