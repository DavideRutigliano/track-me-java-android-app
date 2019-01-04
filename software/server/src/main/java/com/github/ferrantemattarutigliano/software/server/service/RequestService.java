package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.ReceivedRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.SentRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

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

    private int GROUP_REQUEST_ANONYMIZATION_LIMIT = 1000;

    public void addCurrentDateTime(Request request) {
        java.util.Date date = new java.util.Date();
        request.setDate(new Date(date.getTime()));
        request.setTime(new Time(date.getTime()));
    }

    public String individualRequest(IndividualRequest individualRequest) {
        addCurrentDateTime(individualRequest);

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

        String username = receiver.getUser().getUsername();

        simpMessagingTemplate
                .convertAndSend("/request/" + username,
                        "New individual request from: " + sender.getOrganizationName()
                                + ". Received: " + individualRequest.getDate()
                                + ", at: " + individualRequest.getTime() + ".");

        return Message.REQUEST_SUCCESS.toString() + " Receiver: " + username;
    }

    public String groupRequest(GroupRequest groupRequest) {
        addCurrentDateTime(groupRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user == null || !thirdPartyRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Specification<Individual> specification = IndividualSpecification.findByCriteriaSpecification(groupRequest.getCriteria().split(";"));

        Collection<Individual> receivers = new ArrayList<>();

        if (specification != null)
            Optional.ofNullable(individualRepository.findAll(specification)).ifPresent(receivers::addAll);


        if (receivers.size() < GROUP_REQUEST_ANONYMIZATION_LIMIT)
            return Message.REQUEST_NOT_ANONYMOUS.toString();

        ThirdParty sender = thirdPartyRepository.findByUser(user);
        groupRequest.setThirdParty(sender);

        groupRequestRepository.save(groupRequest);

        for (Individual receiver : receivers) {
            String username = receiver.getUser().getUsername();
            simpMessagingTemplate
                    .convertAndSend("/request/" + username,
                            "New group request from: " + sender.getOrganizationName()
                                    + ". Received: " + groupRequest.getDate()
                                    + ", at: " + groupRequest.getTime() + ".");
        }

        if (groupRequest.getSubscription()) {
            simpMessagingTemplate
                    .convertAndSend("/notification/" + groupRequest.getThirdParty().getUser().getUsername(),
                            "Request accepted. Topic: groupreq-" + groupRequest.getId() + ".");
        }

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

    public SentRequestDTO showSentRequest() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ThirdParty sender = null;

        if (user != null) {
            sender = thirdPartyRepository.findByUser(user);
        }

        if (sender != null) {
            ModelMapper modelMapper = new ModelMapper();
            SentRequestDTO sentRequestDTO = new SentRequestDTO();

            Collection<GroupRequest> groupRequest = groupRequestRepository.findByThirdParty(sender);
            Type groupType = new TypeToken<Collection<GroupRequest>>(){}.getType();
            Collection<GroupRequestDTO> groupRequestDTOS = modelMapper.map(groupRequest, groupType);

            Collection<IndividualRequest> individualRequests = individualRequestRepository.findByThirdParty(sender);
            Type individualType = new TypeToken<Collection<IndividualRequest>>(){}.getType();
            Collection<IndividualRequestDTO> individualRequestDTOS = modelMapper.map(individualRequests, individualType);

            sentRequestDTO.setIndividualRequestDTOS(individualRequestDTOS);
            sentRequestDTO.setGroupRequestDTOS(groupRequestDTOS);

            return sentRequestDTO;
        }
        return null;
    }

    public Collection<ReceivedRequestDTO> showIncomingRequest() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Individual receiver = null;

        if (user != null) {
            receiver = individualRepository.findByUser(user);
        }

        if (receiver != null) {
            Collection<IndividualRequest> individualRequests = individualRequestRepository.findBySsn(receiver.getSsn());
            Collection<ReceivedRequestDTO> receivedRequestDTOS = new HashSet<>();
            for(IndividualRequest r : individualRequests){
                if(r.isAccepted() != null) continue; //only not accepted/rejected requests
                ReceivedRequestDTO receivedRequestDTO = new ReceivedRequestDTO();
                String thirdPartyName = r.getThirdParty().getOrganizationName();

                receivedRequestDTO.setId(r.getId());
                receivedRequestDTO.setDate(r.getDate());
                receivedRequestDTO.setTime(r.getTime());
                receivedRequestDTO.setThirdParty(thirdPartyName);
                receivedRequestDTOS.add(receivedRequestDTO);
            }
            return receivedRequestDTOS;
        }
        return null;
    }

    public String handleRequest(Long id, boolean accepted) {
        individualRequestRepository.handleRequest(id, accepted);
        IndividualRequest individualRequest = null;

        if (individualRequestRepository.findById(id).isPresent())
            individualRequest = individualRequestRepository.findById(id).get();

        if (accepted) {
            String receiver = individualRepository.findBySsn(individualRequest.getSsn()).getUser().getUsername();

            simpMessagingTemplate
                    .convertAndSend("/notification/" +
                                    individualRequest.getThirdParty().getUser().getUsername(),
                            "Request accepted. Topic: " + receiver + ".");

            return Message.REQUEST_ACCEPTED.toString();
        }
        return Message.REQUEST_REJECTED.toString();
    }

    public Collection<HealthData> showIndividualData(IndividualRequest individualRequest) {

        IndividualRequest request = individualRequestRepository.findById(individualRequest.getId()).get();

        String ssn = individualRequest.getSsn();

        if (individualRequestRepository.isSubscriptionRequest(request.getId())) {
            return healthDataRepository.findByIndividual(individualRepository.findBySsn(ssn));
        }
        Date date = request.getDate();
        Time time = request.getTime();
        return healthDataRepository.findUntilTimestamp(ssn, date, time);
    }

    public Collection<HealthData> showGroupData(GroupRequest groupRequest) {

        GroupRequest request = groupRequestRepository.findById(groupRequest.getId()).get();

        Specification<Individual> specification = IndividualSpecification.findByCriteriaSpecification(request.getCriteria().split(";"));

        Collection<Individual> receivers = new LinkedHashSet<>();
        Collection<HealthData> healthData = new LinkedHashSet<>();

        if (specification != null)
            Optional.ofNullable(individualRepository.findAll(specification)).ifPresent(receivers::addAll);

        if (groupRequestRepository.isSubscriptionRequest(request.getId())) {
            for (Individual i : receivers) {
                healthData.addAll(healthDataRepository.findByIndividual(individualRepository.findBySsn(i.getSsn())));
            }
        } else {
            for (Individual i : receivers) {
                healthData.addAll(healthDataRepository.findUntilTimestamp(i.getSsn(), request.getDate(), request.getTime()));
            }
        }
        return healthData;
    }
}
