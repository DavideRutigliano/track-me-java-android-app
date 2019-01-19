package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualModifyRunTask;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualShowRunsTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualWatchedRunsView;

import java.util.Collection;

public class IndividualWatchedRunsPresenter extends Presenter<IndividualWatchedRunsView> {
    public IndividualWatchedRunsPresenter(IndividualWatchedRunsView view) {
        super(view);
    }

    public void doFetchRun(){
        new IndividualShowRunsTask("watched", new AsyncResponse<Collection<RunDTO>>() {
            @Override
            public void taskFinish(Collection<RunDTO> output) {
                if(output.isEmpty()){
                    view.noWatchedRuns();
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

    public void unwatchRun(Long runId){
        new IndividualModifyRunTask(runId,"unwatch", new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onRunUnwatch(output);
            }

            @Override
            public void taskFailMessage(String message) {

            }
        }).execute();
    }
}
