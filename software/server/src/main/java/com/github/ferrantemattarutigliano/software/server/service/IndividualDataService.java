package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.GroupRequestRepository;
import com.github.ferrantemattarutigliano.software.server.repository.HealthDataRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

@Service
public class IndividualDataService {

    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private HealthDataRepository healthDataRepository;
    @Autowired
    private GroupRequestRepository groupRequestRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public String insertData(Collection<HealthData> healthData) {

        for (HealthData data : healthData) {
            addCurrentDateTime(data);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Individual individual = individualRepository.findByUser(user);

        for (HealthData data : healthData) {
            data.setIndividual(individual);
            healthDataRepository.save(data);
            simpMessagingTemplate
                    .convertAndSend("/healthdata/" + individual.getUser().getUsername(),
                            "Name: " + data.getName() + ", value: " + data.getValue());
        }

        return Message.INSERT_DATA_SUCCESS.toString();
    }

    @Scheduled(cron = "0 0 8 * * *") //scheduled at 8 o' clock every day
    public void updateGroupRequestTopics() {

        Collection<GroupRequest> groupRequests = groupRequestRepository.findSubscriptionRequest();

        for (GroupRequest groupRequest : groupRequests) {

            Collection<Individual> receivers = new LinkedHashSet<>();
            Collection<HealthData> healthData = new LinkedHashSet<>();

            String criteria = groupRequest.getCriteria();

            Specification<Individual> specification = IndividualSpecification.findByCriteriaSpecification(criteria.split(";"));

            if (specification != null)
                Optional.ofNullable(individualRepository.findAll(specification)).ifPresent(receivers::addAll);

            for (Individual i : receivers) {
                healthData.addAll(healthDataRepository.findByIndividual(individualRepository.findBySsn(i.getSsn())));
            }

            for (HealthData data : healthData) {
                simpMessagingTemplate
                        .convertAndSend("/healthdata/groupreq-" + groupRequest.getId(),
                                "Name: " + data.getName() + ", value: " + data.getValue());
            }
        }
    }

    private void addCurrentDateTime(HealthData healthData) {
        java.util.Date date = new java.util.Date();
        healthData.setDate(new Date(date.getTime()));
        healthData.setTime(new Time(date.getTime()));
    }


}
