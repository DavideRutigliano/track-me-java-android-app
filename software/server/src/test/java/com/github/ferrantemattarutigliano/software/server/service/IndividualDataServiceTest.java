package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.swing.text.html.HTMLDocument;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class IndividualDataServiceTest {

    @InjectMocks
    private IndividualDataService individualDataService;
    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private Authentication mockAuthentication;
    @Mock
    private Principal mockPrincipal;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private IndividualRepository mockIndividualRepository;
    @Mock
    private HealthDataRepository mockHealthDataRepository;
    @Mock
    private SimpMessagingTemplate mockSimpMessagingTemplate;
    @Mock
    private GroupRequestRepository mockGroupRequestRepository;
    @Mock
    private PositionRepository mockPositionRepository;

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

    private GroupRequest createMockGroupRequest(String criteria) {
        GroupRequest request = new GroupRequest(criteria);
        request.setDate(new Date(1));
        request.setTime(new Time(1));

        return request;
    }

    private Position createMockPosition() {
        Position position = new Position("10.0", "30.0");
        return position;
    }

    @Test
    public void insertDataTest() {
        //create mocked individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        //create mocked health data
        ArrayList<HealthData> healthData = new ArrayList<>();
        HealthData firstData = new HealthData();
        firstData.setIndividual(mockedIndividual);
        firstData.setName("hearthbeat");
        firstData.setValue("100");
        HealthData secondData = new HealthData();
        secondData.setIndividual(mockedIndividual);
        secondData.setName("hearthbeat");
        secondData.setValue("200");
        healthData.add(firstData);
        healthData.add(secondData);

        //start test
        mockIndividualAuthorized(mockedUser, mockedIndividual);
        String result = individualDataService.insertData(healthData);
        Assert.assertEquals(Message.INSERT_DATA_SUCCESS.toString(), result);
    }

    @Test
    public void insertDataTest_badRequest() {
        //create mocked individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        //create mocked health data
        ArrayList<HealthData> healthData = new ArrayList<>();
        HealthData firstData = new HealthData();
        firstData.setIndividual(mockedIndividual);
        firstData.setName("hearthbeat");
        firstData.setValue("100");
        HealthData secondData = new HealthData();
        secondData.setIndividual(mockedIndividual);
        secondData.setName("hearthbeat");
        secondData.setValue("200");
        healthData.add(firstData);
        healthData.add(secondData);

        //start test
        mockIndividualAuthorized(mockedUser, mockedIndividual);
        Mockito.when(mockIndividualRepository.existsByUser(mockedUser))
                .thenReturn(false);
        String result = individualDataService.insertData(healthData);
        Assert.assertEquals(Message.BAD_REQUEST.toString(), result);
    }

    @Test
    public void insertPositionTest() {
        //create mocked individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        //create mocked position
        Position position = createMockPosition();


        //start test
        mockIndividualAuthorized(mockedUser, mockedIndividual);

        Mockito.when(mockIndividualRepository.existsByUser(mockedUser))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findByUser(mockedUser))
                .thenReturn(mockedIndividual);
        Mockito.when(mockPositionRepository.save(position))
                .thenReturn(position);

        individualDataService.insertPosition(position);


    }

    @Test
    public void updateGroupRequestTopic() {
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
        //create group requests
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

        //start test
        Mockito.when(mockGroupRequestRepository.findSubscriptionRequest())
                .thenReturn(groupRequests);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Iterator<Individual> it1 = listIndividuals.iterator();
        while (i < 2) {
            Individual IND = it1.next();
            Mockito.when(mockIndividualRepository.findBySsn(IND.getSsn()))
                    .thenReturn(IND);
            Mockito.when((mockHealthDataRepository.findByIndividual(IND)))
                    .thenReturn(healthDatas);
        }

        individualDataService.updateGroupRequestTopics();

    }

}