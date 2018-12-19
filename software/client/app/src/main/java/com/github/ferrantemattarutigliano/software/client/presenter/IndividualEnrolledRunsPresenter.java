package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualModifyRunTask;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualShowRunsTask;
import com.github.ferrantemattarutigliano.software.client.view.IndividualEnrolledRunsView;

import java.util.Collection;

public class IndividualEnrolledRunsPresenter extends Presenter<IndividualEnrolledRunsView> {
    public IndividualEnrolledRunsPresenter(IndividualEnrolledRunsView view) {
        super(view);
    }

    public void doFetchRun(){
        new IndividualShowRunsTask("enrolled", new AsyncResponse<Collection<RunDTO>>() {
            @Override
            public void taskFinish(Collection<RunDTO> output) {
                if(output.isEmpty()){
                    view.noEnrolledRuns();
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

    public void unrollRun(Long runId){
        new IndividualModifyRunTask(runId, "unenroll", new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRunUnroll(output);
            }

            @Override
            public void taskFailMessage(String message) {

            }
        }).execute();
    }
}
