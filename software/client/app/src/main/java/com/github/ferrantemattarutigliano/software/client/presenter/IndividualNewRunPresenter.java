package com.github.ferrantemattarutigliano.software.client.presenter;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualCreateRunTask;
import com.github.ferrantemattarutigliano.software.client.view.IndividualNewRunView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class IndividualNewRunPresenter extends Presenter<IndividualNewRunView>{
    private Collection<PositionDTO> positionDTOS;
    public IndividualNewRunPresenter(IndividualNewRunView view) {
        super(view);
        positionDTOS = new ArrayList<>();
    }

    public void doCreateRun(String title, java.sql.Date date, Time time){
        RunDTO runDTO = new RunDTO();
        runDTO.setDate(date);
        runDTO.setTime(time);
        runDTO.setTitle(title);
        runDTO.setPath(positionDTOS);

        if(positionDTOS.size() < 2){ //we want at least a start and an end
            view.pathTooShort();
            return;
        }

        new IndividualCreateRunTask(runDTO, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String output) {
                view.onCreateRunSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onCreateRunFail(message);
            }
        }).execute();
    }

    public void setPositionDTOS(Collection<PositionDTO> positionDTOS) {
        this.positionDTOS = positionDTOS;
    }
}
