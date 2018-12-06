package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.message.Message;
import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.repository.GroupRequestRepository;
import com.github.ferrantemattarutigliano.software.server.repository.HealthDataRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private HealthDataRepository healthDataRepository;

    public String individualRequest(IndividualRequest individualRequest){ //TODO Add subscription topic

        String ssn = individualRequest.getSsn();
        if (individualRepository.existsBySsn(ssn)) {
            individualRequestRepository.save(individualRequest);
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

    public String handleRequest(Long id, boolean accepted) {

        if (individualRequestRepository.handleRequest(id, accepted))
            return "Success!";
        else return "Failure!";
    }


}
