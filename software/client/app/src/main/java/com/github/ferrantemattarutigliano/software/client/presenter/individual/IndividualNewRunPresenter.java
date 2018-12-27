package com.github.ferrantemattarutigliano.software.client.presenter.individual;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.PositionDTO;
import com.github.ferrantemattarutigliano.software.client.model.RunDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualCreateRunTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualNewRunView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class IndividualNewRunPresenter extends Presenter<IndividualNewRunView> {
    private ArrayList<PositionDTO> positionDTOS;
    private Date date;
    private Time time;
    public IndividualNewRunPresenter(IndividualNewRunView view) {
        super(view);
        positionDTOS = new ArrayList<>();
    }

    public void doCreateRun(String title){
        RunDTO runDTO = new RunDTO();
        runDTO.setTitle(title);
        runDTO.setDate(date);
        runDTO.setTime(time);
        runDTO.setPath(positionDTOS);
        java.util.Date currentDateTime = new java.util.Date();
        Date currentDate = new Date(currentDateTime.getTime());
        Time currentTime = new Time(currentDateTime.getTime());
        if(title.isEmpty()){
            view.onCreateRunFail("Run must have a title.");
            return;
        }
        if(date == null || time == null){
            view.onCreateRunFail("Run must have a starting date.");
            return;
        }
        if(date.before(currentDate) || (date.equals(currentDate) && time.before(currentTime))){
            view.onCreateRunFail("Run can't start in the past.");
            return;
        }
        if(positionDTOS.size() < 2){ //we want at least a start and an end
            view.onCreateRunFail("Run path is too short. Add at least 2 way points.");
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

    public void setPositionDTOS(ArrayList<PositionDTO> positionDTOS) {
        this.positionDTOS = positionDTOS;
    }

    public boolean isPathSet(){
        return positionDTOS != null && !positionDTOS.isEmpty();
    }

    public ArrayList<PositionDTO> getPositionDTOS() {
        return positionDTOS;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
