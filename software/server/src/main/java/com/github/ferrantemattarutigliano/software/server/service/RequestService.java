package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.message.Message;
import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RequestService {
    @Autowired
    private IndividualRequestRepository individualRequestRepository;
    @Autowired
    private GroupRequestRepository groupRequestRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private ThirdPartyRepository thirdPartysRepository;
    @Autowired
    private HealthDataRepository healthDataRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public String individualRequest(IndividualRequest individualRequest){ //TODO Add subscription topic

        String ssn = individualRequest.getSsn();
        if (individualRepository.existsBySsn(ssn)) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            ThirdParty sender = thirdPartysRepository.findByUser(user);

            individualRequest.setThirdParty(sender);
            individualRequestRepository.save(individualRequest);

            Individual receiver = individualRepository.findBySsn(ssn);
            String username = receiver.getUser().getUsername();
            simpMessagingTemplate.setUserDestinationPrefix(username);

            simpMessagingTemplate
                    .convertAndSendToUser(username,
                            "/request/",
                            "request from: " +
                                    individualRequest.getThirdParty().getOrganizationName() +
                                    ", sent: " +
                                    individualRequest.getTimestamp());

            return Message.REQUEST_SUCCESS.toString();
        }
        return Message.REQUEST_INVALID_SSN.toString();
    }

    public String groupRequest(GroupRequest groupRequest){ //TODO Add subscription topic

        String criteria = groupRequest.getCriteria();

        return Message.REQUEST_SUCCESS.toString();
    }

    public Collection<IndividualRequest> showSentIndividualRequest(Long id) {

        return individualRequestRepository.findByThirdParty(id);
    }

    public Collection<GroupRequest> showSentGroupRequest(Long id) {

        return groupRequestRepository.findByThirdParty(id);
    }

    public Collection<IndividualRequest> showIncomingRequest(String ssn) {

        return individualRequestRepository.findBySsn(ssn);
    }

    public boolean handleRequest(Long id, boolean accepted) {

        return individualRequestRepository.handleRequest(id, accepted);
    }


}
