package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.dto.*;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Type;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class RequestControllerTest {
    @InjectMocks
    private RequestController requestController;

    @Mock
    private RequestService mockRequestService;


    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
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

    @Test
    public void showSentGroupRequestTest() {
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

        Mockito.when(mockRequestService.showSentGroupRequest())
                .thenReturn(groupRequests);
        Collection<GroupRequest> result = requestController.showSentGroupRequest();

        Assert.assertEquals(groupRequests, result);
    }


    @Test
    public void showAllSentRequestTest() {
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
        GroupRequest firstGroupRequest = createMockGroupRequest("state=italy;");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        //create individual requests
        IndividualRequest firstIndRequest = createMockIndRequest(mockedIndividual.getSsn());
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);
        //create sent request DTO
        ModelMapper modelMapper = new ModelMapper();
        SentRequestDTO sentRequestDTO = new SentRequestDTO();
        //group request DTO
        Type groupType = new TypeToken<Collection<GroupRequest>>() {
        }.getType();
        Collection<GroupRequestDTO> groupRequestDTOS = modelMapper.map(groupRequests, groupType);
        //individual request DTO
        Type individualType = new TypeToken<Collection<IndividualRequest>>() {
        }.getType();
        Collection<IndividualRequestDTO> individualRequestDTOS = modelMapper.map(indRequests, individualType);
        //set them
        sentRequestDTO.setIndividualRequestDTOS(individualRequestDTOS);
        sentRequestDTO.setGroupRequestDTOS(groupRequestDTOS);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.showSentRequest())
                .thenReturn(sentRequestDTO);
        SentRequestDTO result = requestController.showAllSentRequests();

        Assert.assertEquals(sentRequestDTO, result);

    }


    @Test
    public void showIncomingRequestTest() {
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
        firstIndRequest.setAccepted(true);
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);
        //create collection of request DTO
        Collection<ReceivedRequestDTO> receivedRequestDTOS = new HashSet<>();
        for (IndividualRequest r : indRequests) {
            if (r.isAccepted() != null) continue; //only not accepted/rejected requests
            ReceivedRequestDTO receivedRequestDTO = new ReceivedRequestDTO();
            String thirdPartyName = r.getThirdParty().getOrganizationName();

            receivedRequestDTO.setId(r.getId());
            receivedRequestDTO.setDate(r.getDate());
            receivedRequestDTO.setTime(r.getTime());
            receivedRequestDTO.setThirdParty(thirdPartyName);
            receivedRequestDTOS.add(receivedRequestDTO);
        }

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.showIncomingRequest())
                .thenReturn(receivedRequestDTOS);
        Collection<ReceivedRequestDTO> result = requestController.showIncomingRequest();

        Assert.assertEquals(receivedRequestDTOS, result);

    }

    @Test
    public void handleRequestTest_Accepted() {
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
        firstIndRequest.setId(0L);
        firstIndRequest.setThirdParty(mockedThirdParty);
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.handleRequest(0L, true))
                .thenReturn(Message.REQUEST_ACCEPTED.toString());

        String result = requestController.handleRequest(mockedIndividual.getUser().getUsername(), 0L, true);

        Assert.assertEquals(Message.REQUEST_ACCEPTED.toString(), result);

    }

    @Test
    public void handleRequestTest_Refused() {
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
        firstIndRequest.setId(0L);
        firstIndRequest.setThirdParty(mockedThirdParty);
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.handleRequest(0L, false))
                .thenReturn(Message.REQUEST_REJECTED.toString());

        String result = requestController.handleRequest(mockedIndividual.getUser().getUsername(), 0L, false);

        Assert.assertEquals(Message.REQUEST_REJECTED.toString(), result);

    }

    @Test
    public void showIndividualDataTest() {
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
        firstIndRequest.setId(0L);
        firstIndRequest.setThirdParty(mockedThirdParty);
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);
        //create health data
        Date bdate = new Date(1);
        HealthData firstHealthData = new HealthData("high pressure", "130", bdate);
        //add to a collection of healthdata
        Collection<HealthData> healthDatas = new ArrayList<>();
        healthDatas.add(firstHealthData);

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.showIndividualData(firstIndRequest.getId()))
                .thenReturn(healthDatas);

        Collection<HealthData> result = requestController.showIndividualData(firstIndRequest.getId());

        Assert.assertEquals(healthDatas, result);

    }


    @Test
    public void showGroupDataTest_Subscripted() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 2; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti" + x);
            int Ssn = 100000000;
            Ssn = Ssn + i;
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setState("italy");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create individual requests
        GroupRequest firstGroupRequest = createMockGroupRequest("state=italy;");
        firstGroupRequest.setSubscription(true);
        firstGroupRequest.setId(0L);
        firstGroupRequest.setThirdParty(mockedThirdParty);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        //create health data
        Date bdate = new Date(1);
        HealthData firstHealthData = new HealthData("high pressure", "130", bdate);
        //add to a collection of healthdata
        Collection<HealthData> healthDatas = new ArrayList<>();
        for (i = 0; i < 1; i++) {
            healthDatas.add(firstHealthData);
        }
        //create collection of collections of health data

        Collection<Collection<HealthData>> colHealthDatas = new ArrayList<>();
        for (i = 0; i < 2; i++) {
            colHealthDatas.add(healthDatas);
        }
        //create expected result
        Collection<HealthData> healthDatasEX = new ArrayList<>();
        for (i = 0; i < 2; i++) {
            healthDatasEX.add(firstHealthData);
        }

        /* TEST STARTS HERE */

        Mockito.when(mockRequestService.showGroupData(firstGroupRequest.getId()))
                .thenReturn(healthDatasEX);

        Collection<HealthData> result = requestController.showGroupData(firstGroupRequest.getId());

        Assert.assertEquals(healthDatasEX, result);

    }

}
