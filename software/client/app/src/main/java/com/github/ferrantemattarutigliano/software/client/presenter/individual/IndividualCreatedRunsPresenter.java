package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualDeleteRunTask;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualShowRunsTask;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualStartRunTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualCreatedRunsView;

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

    public void doStartRun(Long runId){
        new IndividualStartRunTask(runId, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onStartRun(output);
            }

            @Override
            public void taskFailMessage(String message) {

            }
        }).execute();
    }

    public void doDeleteRun(Long runId){
        new IndividualDeleteRunTask(runId, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onDeleteRun(output);
            }

            @Override
            public void taskFailMessage(String message) {

            }
        }).execute();
    }
}
