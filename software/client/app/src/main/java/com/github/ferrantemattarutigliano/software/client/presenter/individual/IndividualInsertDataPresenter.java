package com.github.ferrantemattarutigliano.software.client.presenter.individual;


import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.task.individual.IndividualInsertDataTask;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualInsertDataView;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            String value = StringUtils.substringBetween(data, ":", ". Received at: ");
            String dateString = StringUtils.substringBetween(data, ". Received at: ", "_");
            String timeString = StringUtils.substringAfter(data, "_");
            Date date = null;
            try {
                date = new SimpleDateFormat().parse(dateString);
            } catch (ParseException e) {
            }
            Time time = null;
            try {
                time = new Time(new SimpleDateFormat("hh:mm:ss").parse(timeString).getTime());
            } catch (ParseException e) {
            }
            HealthDataDTO h = new HealthDataDTO();
            h.setName(name);
            h.setValue(value);
            h.setDate(date);
            h.setTime(time);
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
