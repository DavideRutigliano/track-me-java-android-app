package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.*;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RequestControllerTest {
    @InjectMocks
    private RequestController requestController;

    @Mock
    private RequestService mockRequestService;

    @Mock
    private IndividualRepository mockIndividualRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private SecurityContext mockSecurityContext;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private Principal mockPrincipal;

    @Mock
    private ThirdPartyRepository mockThirdPartyRepository;

    @Mock
    private IndividualRequestRepository mockIndividualRequestRepository;

    @Mock
    private SimpMessagingTemplate mockSimpMessaggingTemplate;

    @Mock
    private IndividualSpecification mockIndividualSpecification;

    @Mock
    private GroupRequestRepository mockGroupRequestRepository;

    @Mock
    private HealthDataRepository mockHealthDataRepository;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    private void mockIndividualAuthorized(User expectedUser, Individual expectedIndividual) {
        SecurityContextHolder.setContext(mockSecurityContext);

        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(mockPrincipal);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(expectedUser);
        Mockito.when(mockUserRepository.existsByUsername(expectedUser.getUsername()))
                .thenReturn(true);
        //mock the existing individual associated with the user
        Mockito.when(mockIndividualRepository.existsByUser(expectedUser))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findByUser(expectedUser))
                .thenReturn(expectedIndividual);
    }


    private void mockThirdPartyAuthorized(User expectedUser, ThirdParty expectedThirdParty) {
        SecurityContextHolder.setContext(mockSecurityContext);

        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(mockPrincipal);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(expectedUser);
        Mockito.when(mockUserRepository.existsByUsername(expectedUser.getUsername()))
                .thenReturn(true);
        //mock the existing third party associated with the user
        Mockito.when(mockThirdPartyRepository.existsByUser(expectedUser))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(expectedUser))
                .thenReturn(expectedThirdParty);
    }

    private IndividualRequest createMockIndRequest(String ssn) {
        IndividualRequest request = new IndividualRequest(ssn);
        request.setDate(new Date(1));
        request.setTime(new Time(1));

        return request;

    }

    private GroupRequest createMockGroupRequest(String criteria) {
        GroupRequest request = new GroupRequest(criteria);
        request.setDate(new Date(1));
        request.setTime(new Time(1));

        return request;
    }

    @Test
    public void individualRequestTest() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create individual requests
        IndividualRequest firstIndRequest = createMockIndRequest(mockedIndividual.getSsn());
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.individualRequest(firstIndRequest))
                .thenReturn(Message.REQUEST_SUCCESS.toString() + " Receiver: " + mockedIndividual.getUser().getUsername());

        String result = requestController.individualRequest(firstIndRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString() + " Receiver: " + mockedIndividual.getUser().getUsername(), result);
    }

    @Test
    public void groupRequestTest() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("state=italy");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.groupRequest(firstGroupRequest))
                .thenReturn(Message.REQUEST_SUCCESS.toString());

        String result = requestController.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);
    }


    @Test
    public void showSentIndividualRequestTest() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create individual requests
        IndividualRequest firstIndRequest = createMockIndRequest(mockedIndividual.getSsn());
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.showSentIndividualRequest())
                .thenReturn(indRequests);
        Collection<IndividualRequest> result = requestController.showSentIndividualRequest();

        Assert.assertEquals(indRequests, result);
    }



}
