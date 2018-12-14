package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

import static com.github.ferrantemattarutigliano.software.server.repository.IndividualSpecification.*;

@Service
public class RequestService {

    @Autowired
    private IndividualRequestRepository individualRequestRepository;
    @Autowired
    private GroupRequestRepository groupRequestRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private HealthDataRepository healthDataRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public String individualRequest(IndividualRequest individualRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        String ssn = individualRequest.getSsn();

        if (user == null || !thirdPartyRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        ThirdParty sender = thirdPartyRepository.findByUser(user);

        if (!individualRepository.existsBySsn(ssn)) {
            return Message.REQUEST_INVALID_SSN.toString();
        }

        individualRequest.setThirdParty(sender);
        individualRequestRepository.save(individualRequest);

        Individual receiver = individualRepository.findBySsn(ssn);

        //TODO Add subscription topic
        String username = receiver.getUser().getUsername();

        simpMessagingTemplate
                .convertAndSendToUser(username, "/server/request", "request from: " +
                        individualRequest.getThirdParty().getOrganizationName() + ", sent: " +
                                individualRequest.getTimestamp());

        return Message.REQUEST_SUCCESS.toString();
    }

    public String groupRequest(GroupRequest groupRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user == null || !thirdPartyRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Specification<Individual> specification = IndividualSpecification.findByCriteriaSpecification(groupRequest.getCriteria().split(";"));

        Collection<Individual> receivers = new LinkedHashSet<>();

        if (specification != null)
            Optional.ofNullable(individualRepository.findAll(specification)).ifPresent(receivers::addAll);

        if (receivers.size() < 1000)
            return Message.REQUEST_NOT_ANONYMOUS.toString();

        ThirdParty sender = thirdPartyRepository.findByUser(user);
        groupRequest.setThirdParty(sender);

        groupRequestRepository.save(groupRequest);

        //TODO Add subscription topic

        return Message.REQUEST_SUCCESS.toString();
    }

    public Collection<IndividualRequest> showSentIndividualRequest() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ThirdParty sender = null;

        if (user != null) {
            sender = thirdPartyRepository.findByUser(user);
        }

        if (sender != null) {
            return individualRequestRepository.findByThirdParty(sender);
        } else return null;
    }

    public Collection<GroupRequest> showSentGroupRequest() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ThirdParty sender = null;

        if (user != null) {
            sender = thirdPartyRepository.findByUser(user);
        }

        if (sender != null) {
            return groupRequestRepository.findByThirdParty(sender);
        } else return null;
    }

    public Collection<IndividualRequest> showIncomingRequest() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Individual receiver = null;

        if (user != null) {
            receiver = individualRepository.findByUser(user);
        }

        if (receiver != null) {
            return individualRequestRepository.findBySsn(receiver.getSsn());
        } else return null;
    }

    public String handleRequest(Long id, boolean accepted) {

        individualRequestRepository.handleRequest(id, accepted);

        if (accepted)
            return Message.REQUEST_ACCEPTED.toString();
        else
        return Message.REQUEST_REJECTED.toString();
    }

    public Collection<HealthData> showIndividualData(IndividualRequest individualRequest) {

        IndividualRequest request = individualRequestRepository.findById(individualRequest.getId()).get();

        String ssn = individualRequest.getSsn();

        if (individualRequestRepository.isSubscriptionRequest(request.getId())) {
            return healthDataRepository.findByIndividual(individualRepository.findBySsn(ssn));
        } else {
            Date timestamp = request.getTimestamp();
            return healthDataRepository.findUntilTimestamp(ssn, timestamp);
        }
    }

    public Collection<HealthData> showGroupData(GroupRequest groupRequest) {

        GroupRequest request = groupRequestRepository.findById(groupRequest.getId()).get();

        if (groupRequestRepository.isSubscriptionRequest(request.getId())) {

        } else {

        }
        return null;
    }
}
