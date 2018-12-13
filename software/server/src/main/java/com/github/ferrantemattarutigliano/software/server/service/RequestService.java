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
                .convertAndSend("/request",
                        "request from: " +
                                individualRequest.getThirdParty().getOrganizationName() +
                                ", sent: " +
                                individualRequest.getTimestamp());

        return Message.REQUEST_SUCCESS.toString();
    }

    public String groupRequest(GroupRequest groupRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user == null || !thirdPartyRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        String[] criteria = groupRequest.getCriteria().split(";");

        Collection<Individual> receivers = new LinkedHashSet<>();

        Specification<Individual> specification = null;

        for (String criterion : criteria) {

            if (criterion.contains("state")) {
                String state = StringUtils.substringAfter(criterion, "state=");

                specification = (specification != null) ? specification.and(inState(state)) : inState(state);
            }

            if (criterion.contains("city")) {
                String city = StringUtils.substringAfter(criterion, "city=");

                specification = (specification != null) ? specification.and(inCity(city)) : inCity(city);
            }

            if (criterion.contains("address")) {
                String address = StringUtils.substringAfter(criterion, "address=");

                specification = (specification != null) ? specification.and(withAddress(address)) : withAddress(address);
            }

            if (criterion.contains("firstname")) {
                String firstname;
                if (criterion.contains("like")) {
                    firstname = StringUtils.substringAfter(criterion, "like-firstname=");

                    specification = (specification != null) ? specification.and(likeFirstname(firstname)) : likeFirstname(firstname);
                } else {
                    firstname = StringUtils.substringAfter(criterion, "firstname=");

                    specification = (specification != null) ? specification.and(withFirstname(firstname)) : withFirstname(firstname);
                }
            }

            if (criterion.contains("lastname")) {
                String lastname;

                if (criterion.contains("like")) {
                    lastname = StringUtils.substringAfter(criterion, "like-lastname=");

                    specification = (specification != null) ? specification.and(likeLastname(lastname)) : likeLastname(lastname);
                } else {
                    lastname = StringUtils.substringAfter(criterion, "lastname=");

                    specification = (specification != null) ? specification.and(withLastname(lastname)) : withLastname(lastname);
                }
            }

            if (criterion.contains("birtdate")) {

                if (criterion.contains(",")) {
                    String from = StringUtils.substringBetween(criterion, "birthdate:", ",");
                    String to = StringUtils.substringAfter(criterion, ",");

                    specification = (specification != null) ? specification.and(beetweenBirthDates(from, to)) : beetweenBirthDates(from, to);
                } else {
                    String birtdate;
                    if (criterion.contains("<")) {

                        if (criterion.contains("=")) {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate<=");

                            specification = (specification != null)
                                    ? specification.and(lessThanBirthDate(birtdate).or(withBirthDate(birtdate)))
                                    : lessThanBirthDate(birtdate).or(withBirthDate(birtdate));
                        } else {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate<");

                            specification = (specification != null) ? specification.and(lessThanBirthDate(birtdate)) : lessThanBirthDate(birtdate);
                        }
                    } else if (criterion.contains(">")) {

                        if (criterion.contains("=")) {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate>=");

                            specification = (specification != null)
                                    ? specification.and(greaterThanBirthDate(birtdate).or(withBirthDate(birtdate)))
                                    : greaterThanBirthDate(birtdate).or(withBirthDate(birtdate));
                        } else {
                            birtdate = StringUtils.substringAfter(criterion, "birthdate>");

                            specification = (specification != null) ? specification.and(greaterThanBirthDate(birtdate)) : greaterThanBirthDate(birtdate);
                        }
                    } else if (criterion.contains("=")) {
                        birtdate = StringUtils.substringAfter(criterion, "birthdate=");

                        specification = (specification != null) ? specification.and(withBirthDate(birtdate)) : withBirthDate(birtdate);
                    }
                }
            }

            if (criterion.contains("height")) {
                String height;

                if (criterion.contains("<")) {
                    if (criterion.contains("=")) {
                        height = StringUtils.substringAfter(criterion, "height<=");

                        specification = (specification != null)
                                ? specification.and(shorterThanHeight(height).or(withHeight(height)))
                                : shorterThanHeight(height).or(withHeight(height));
                    } else {
                        height = StringUtils.substringAfter(criterion, "height<");

                        specification = (specification != null) ? specification.and(shorterThanHeight(height)) : shorterThanHeight(height);
                    }
                } else if (criterion.contains(">")) {
                    if (criterion.contains("=")) {
                        height = StringUtils.substringAfter(criterion, "height>=");

                        specification = (specification != null)
                                ? specification.and(tallerThanHeight(height).or(withHeight(height)))
                                : tallerThanHeight(height).or(withHeight(height));
                    } else {
                        height = StringUtils.substringAfter(criterion, "height>");

                        specification = (specification != null) ? specification.and(tallerThanHeight(height)) : tallerThanHeight(height);
                    }
                } else if (criterion.contains("=")) {
                    height = StringUtils.substringAfter(criterion, "height=");

                    specification = (specification != null) ? specification.and(withHeight(height)) : withHeight(height);

                }
            }

            if (criterion.contains("weight")) {
                String weight;

                if (criterion.contains("<")) {
                    if (criterion.contains("=")) {
                        weight = StringUtils.substringAfter(criterion, "weight<=");

                        specification = (specification != null)
                                ? specification.and(lighterThanWeight(weight).or(withWeight(weight)))
                                : lighterThanWeight(weight).or(withWeight(weight));
                    } else {
                        weight = StringUtils.substringAfter(criterion, "weight<");

                        specification = (specification != null) ? specification.and(lighterThanWeight(weight)) : lighterThanWeight(weight);
                    }
                } else if (criterion.contains(">")) {
                    if (criterion.contains("=")) {
                        weight = StringUtils.substringAfter(criterion, "weight>=");

                        specification = (specification != null)
                                ? specification.and(heavierThanWeight(weight).or(withWeight(weight)))
                                : heavierThanWeight(weight).or(withWeight(weight));
                    } else {
                        weight = StringUtils.substringAfter(criterion, "weight>");

                        specification = (specification != null) ? specification.and(heavierThanWeight(weight)) : heavierThanWeight(weight);
                    }
                } else if (criterion.contains("=")) {
                    weight = StringUtils.substringAfter(criterion, "weight=");

                    specification = (specification != null) ? specification.and(withWeight(weight)) : withWeight(weight);
                }
            }
        }

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

    public String insertData(Collection<HealthData> healthData) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Individual individual = individualRepository.findByUser(user);

        for (HealthData data : healthData) {
            data.setIndividual(individual);
            healthDataRepository.save(data);
        }

        return Message.INSERT_DATA_SUCCESS.toString();
    }

    public Collection<HealthData> showIndividualData(IndividualRequest individualRequest) {

        IndividualRequest request = individualRequestRepository.findById(individualRequest.getId()).get();

        if (individualRequestRepository.isSubscriptionRequest(request.getId())) {
            return healthDataRepository.findAll();
        } else {
            return healthDataRepository.findUntilTimestamp(request.getTimestamp());
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
