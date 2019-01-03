package com.github.ferrantemattarutigliano.software.client.presenter.individual;


import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualInsertDataTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualInsertDataView;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class IndividualInsertDataPresenter extends Presenter<IndividualInsertDataView>  {
    public IndividualInsertDataPresenter(IndividualInsertDataView view) {
        super(view);
    }

    public void doInsertData(String healthDataString) {
        String[] healthData = healthDataString.split("\n");
        Collection<HealthDataDTO> healthDataDTOS = new ArrayList<>();
        for (String data : healthData) {
            String name = StringUtils.substringBefore(data, ":");
            String value = StringUtils.substringAfter(data, ":");
            HealthDataDTO h = new HealthDataDTO();
            h.setName(name);
            h.setValue(value);
            h.setDate(new Date());
            h.setTime(new Time(System.currentTimeMillis()));
            healthDataDTOS.add(h);
        }

        new IndividualInsertDataTask(healthDataDTOS, new AsyncResponse<String>() {
            @Override
            public void taskFinish(String message) {
                view.onInsertDataSuccess(message);
            }

            @Override
            public void taskFailMessage(String message) {
                view.onInsertDataFailure(message);
            }
        }).execute();
    }

}
