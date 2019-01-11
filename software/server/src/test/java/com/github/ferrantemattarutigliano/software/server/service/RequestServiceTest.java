package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.ReceivedRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.SentRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Type;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class RequestServiceTest {
    @InjectMocks
    private RequestService requestService;

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

    private IndividualRequest createMockIndRequest2(String ssn) {
        IndividualRequest request = new IndividualRequest();
        request.setSsn(ssn);
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);


        String result = requestService.individualRequest(firstIndRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString() + " Receiver: " + mockedIndividual.getUser().getUsername(), result);


    }

    @Test
    public void individualRequestTest_badRequest() {
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(false);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);


        String result = requestService.individualRequest(firstIndRequest);

        Assert.assertEquals(Message.BAD_REQUEST.toString(), result);


    }

    @Test
    public void individualRequestTest_invalidSsn() {
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(false);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);


        String result = requestService.individualRequest(firstIndRequest);

        Assert.assertEquals(Message.REQUEST_INVALID_SSN.toString(), result);


    }


    @Test
    public void groupRequestTestBadReq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setState("italy");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(false);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.BAD_REQUEST.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_state() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setState("italy");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("state=italy");
        firstGroupRequest.setSubscription(true);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_city() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setCity("Milan");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("city=Milan");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_address() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setAddress("via padova");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("address=via padova");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }


    @Test
    public void groupRequestTestNotAnonymous_firstname() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setFirstname("Gigio");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("firstname=Gigio");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_firstnameLike() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setFirstname("Gigio");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("like-firstname=Gigio");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_lastname() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setLastname("Topo");
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("lastname=Topo");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }



    @Test
    public void groupRequestTestNotAnonymous_birthdateeq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        Date birthDate = new Date(95, 01, 06);
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setBirthdate(birthDate);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate=" + birthDate);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }


    @Test
    public void groupRequestTestNotAnonymous_birthdateeqmag() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        Date birthDate = new Date(95, 01, 06);
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setBirthdate(birthDate);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate>=" + birthDate);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_birthdateeqmin() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        Date birthDate = new Date(95, 01, 06);
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setBirthdate(birthDate);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate>=" + birthDate);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_birthdatemag() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        Date birthDate = new Date(95, 01, 06);
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setBirthdate(birthDate);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(90, 01, 06);
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate>" + birthDate2);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_birthdatemin() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        Date birthDate = new Date(95, 01, 06);
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setBirthdate(birthDate);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 06);
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate<" + birthDate2);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_heighteq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setHeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("height=" + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }



    @Test
    public void groupRequestTestNotAnonymous_heightmageq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setHeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("height>=" + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_heightmineq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setHeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("height<=" + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_heightmag() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setHeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("height>" + 99);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_heightmin() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setHeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("height<" + 101);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_wheighteq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setWeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("weight=" + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }



    @Test
    public void groupRequestTestNotAnonymous_wheightmageq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setWeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("weight>=" + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_wheightmineq() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setWeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("weight<=" + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_wheightmag() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setWeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("weight>" + 99);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_wheightmin() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setWeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("weight<" + 101);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTest_state() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i=0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x=Integer.toString(i);
            User mockedUser = new User("username"+x, "password"+x, "aa@a"+x+"a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo"+x);
            mockedIndividual.setLastname("pippetti"+x);
            int Ssn=100000000;
            Ssn=Ssn+i;
            String z=Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setState("italy");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
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


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_city() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
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
            mockedIndividual.setCity("Milan");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("state=Milan;");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_address() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
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
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("address=via padova;");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_firstname() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo");
            mockedIndividual.setLastname("pippetti" + x);
            int Ssn = 100000000;
            Ssn = Ssn + i;
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("firstname=pippo;");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_lastname() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("lastname=pippetti;");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_birthdateeq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        Date birthDate = new Date(90, 01, 01);
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setBirthdate(birthDate);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate=" + birthDate + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }



    @Test
    public void groupRequestTest_birthdatemineq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        Date birthDate = new Date(90, 01, 01);
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setBirthdate(birthDate);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate<=" + birthDate + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_birthdatemag() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        Date birthDate = new Date(90, 01, 01);
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setBirthdate(birthDate);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(80, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate>" + birthDate2 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_birthdatemin() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        Date birthDate = new Date(90, 01, 01);
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setBirthdate(birthDate);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate<" + birthDate2 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_heighteq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setHeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("height=" + 100 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_heightmageq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setHeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("height>=" + 100 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_heightmineq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setHeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("height<=" + 100 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_heightmag() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setHeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("height>" + 99 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_heightmin() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setHeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("height<" + 101 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_weighteq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setWeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight=" + 100 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_weightmageq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setWeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight>=" + 100 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_weightmineq() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setWeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight<=" + 100 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_weightmag() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setWeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight>" + 99 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTest_weightmin() {
        //create a mock users individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setWeight(100);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight<" + 101 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(indRequests);


        Collection<IndividualRequest> result = requestService.showSentIndividualRequest();

        Assert.assertEquals(indRequests, result);


    }


    @Test
    public void showSentGroupRequestTest() {

        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight<" + 101 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockGroupRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(groupRequests);


        Collection<GroupRequest> result = requestService.showSentGroupRequest();

        Assert.assertEquals(groupRequests, result);

    }

    @Test
    public void showSentGroupRequestTest_Fails() {

        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        Date birthDate2 = new Date(99, 01, 01);
        GroupRequest firstGroupRequest = createMockGroupRequest("weight<" + 101 + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(null);
        Mockito.when(mockGroupRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(groupRequests);

        Collection<GroupRequest> result = requestService.showSentGroupRequest();


    }

    @Test
    public void showSentRequestTest() {

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
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockGroupRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(groupRequests);
        Mockito.when(mockIndividualRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(indRequests);

        SentRequestDTO result = requestService.showSentRequest();

        Assert.assertEquals(sentRequestDTO.getGroupRequestDTOS(), result.getGroupRequestDTOS());
        Assert.assertEquals(sentRequestDTO.getIndividualRequestDTOS(), result.getIndividualRequestDTOS());

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
        IndividualRequest firstIndRequest = createMockIndRequest2(mockedIndividual.getSsn());
        firstIndRequest.setThirdParty(mockedThirdParty);
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
        mockIndividualAuthorized(mockedUser, mockedIndividual);


        Mockito.when(mockIndividualRepository.findByUser(mockedUser2))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRequestRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(indRequests);


        Collection<ReceivedRequestDTO> result = requestService.showIncomingRequest();
        Iterator<ReceivedRequestDTO> Itr = result.iterator();
        for (Iterator<ReceivedRequestDTO> i = receivedRequestDTOS.iterator(); i.hasNext(); ) {
            ReceivedRequestDTO I = i.next();
            ReceivedRequestDTO R = Itr.next();
            Assert.assertEquals(I.getId(), R.getId());
            Assert.assertEquals(I.getAccepted(), R.getAccepted());
            Assert.assertEquals(I.getDate(), R.getDate());
            Assert.assertEquals(I.getThirdParty(), R.getThirdParty());
            Assert.assertEquals(I.getThirdParty(), R.getThirdParty());
            Assert.assertEquals(I.getTime(), R.getTime());


        }
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
        mockIndividualAuthorized(mockedUser, mockedIndividual);


        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);
        Mockito.when(mockIndividualRequestRepository.findById(0L))
                .thenReturn(Optional.of(firstIndRequest));
        Mockito.when(mockIndividualRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(indRequests);


        String result = requestService.handleRequest(0L, true);

        Assert.assertEquals(Message.REQUEST_ACCEPTED.toString(), result);
    }

    @Test
    public void showIndividualDataTest_NotAccepted() {
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
        firstIndRequest.setAccepted(false);
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);
        Mockito.when(mockIndividualRequestRepository.findById(0L))
                .thenReturn(Optional.of(firstIndRequest));


        requestService.showIndividualData(firstIndRequest.getId());
        ;
    }

    @Test
    public void showIndividualDataTest_Subscripted() {
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
        firstIndRequest.setAccepted(true);
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
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);
        Mockito.when(mockIndividualRequestRepository.findById(0L))
                .thenReturn(Optional.of(firstIndRequest));
        Mockito.when(mockIndividualRequestRepository.isSubscriptionRequest(firstIndRequest.getId()))
                .thenReturn(true);
        Mockito.when(mockIndividualRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(indRequests);
        Mockito.when(mockHealthDataRepository.findByIndividual(mockedIndividual))
                .thenReturn(healthDatas);


        Collection<HealthData> result = requestService.showIndividualData(firstIndRequest.getId());

        Assert.assertEquals(healthDatas, result);
    }

    @Test
    public void showIndividualDataTest_NotSubscripted() {
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
        firstIndRequest.setAccepted(true);
        Date reqDate = new Date(98, 10, 01); //TODO DEPRECATED!!!
        firstIndRequest.setDate(reqDate);
        //add request to a collection
        Collection<IndividualRequest> indRequests = new ArrayList<>();
        indRequests.add(firstIndRequest);
        //save it in thirdparty
        mockedThirdParty.setIndividualRequests(indRequests);
        //create health data
        Date bdate = new Date(96, 12, 01); //TODO DEPRECATED!!!
        HealthData firstHealthData = new HealthData("high pressure", "130", bdate);
        //add to a collection of healthdata
        Collection<HealthData> healthDatas = new ArrayList<>();
        healthDatas.add(firstHealthData);




        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);
        Mockito.when(mockIndividualRequestRepository.findById(0L))
                .thenReturn(Optional.of(firstIndRequest));
        Mockito.when(mockIndividualRequestRepository.isSubscriptionRequest(firstIndRequest.getId()))
                .thenReturn(false);
        Mockito.when(mockIndividualRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(indRequests);
        Mockito.when(mockHealthDataRepository.findUntilTimestamp(firstIndRequest.getSsn(), firstIndRequest.getDate(), firstIndRequest.getTime()))
                .thenReturn(healthDatas);


        Collection<HealthData> result = requestService.showIndividualData(firstIndRequest.getId());

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



        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockGroupRequestRepository.findById(0L))
                .thenReturn(Optional.of(firstGroupRequest));
        Mockito.when(mockGroupRequestRepository.isSubscriptionRequest(firstGroupRequest.getId()))
                .thenReturn(true);
        Mockito.when(mockGroupRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(groupRequests);


        Iterator<Individual> z = listIndividuals.iterator();
        Iterator<Collection<HealthData>> l = colHealthDatas.iterator();
        l.hasNext();
        while (z.hasNext() && l.hasNext()) {
            Individual mockIndividual = z.next();
            Collection<HealthData> mockHealthdatas = l.next();
            Mockito.when(mockIndividualRepository.findBySsn(mockIndividual.getSsn()))
                    .thenReturn(mockIndividual);
            Mockito.when((mockHealthDataRepository.findByIndividual(mockIndividual)))
                    .thenReturn(mockHealthdatas);
        }


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);




        Collection<HealthData> result = requestService.showGroupData(firstGroupRequest.getId());


        Assert.assertEquals(healthDatasEX, result);
    }

    @Test
    public void showGroupDataTest_NotSubscripted() {
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
        firstGroupRequest.setSubscription(false);
        firstGroupRequest.setId(0L);
        firstGroupRequest.setThirdParty(mockedThirdParty);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        //create health data
        Date bdate = new Date(1);
        Time time = new Time(1);
        HealthData firstHealthData = new HealthData("high pressure", "130", bdate);
        firstHealthData.setTime(time);
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


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);


        Mockito.when(mockGroupRequestRepository.findById(0L))
                .thenReturn(Optional.of(firstGroupRequest));
        Mockito.when(mockGroupRequestRepository.isSubscriptionRequest(firstGroupRequest.getId()))
                .thenReturn(false);
        Mockito.when(mockGroupRequestRepository.findByThirdParty(mockedThirdParty))
                .thenReturn(groupRequests);


        Iterator<Individual> z = listIndividuals.iterator();
        Iterator<Collection<HealthData>> l = colHealthDatas.iterator();
        l.hasNext();
        while (z.hasNext() && l.hasNext()) {
            Individual mockIndividual = z.next();
            Collection<HealthData> mockHealthdatas = l.next();
            Mockito.when((mockHealthDataRepository.findUntilTimestamp(mockIndividual.getSsn(), firstHealthData.getDate(), firstHealthData.getTime())))
                    .thenReturn(mockHealthdatas);
        }


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);


        Collection<HealthData> result = requestService.showGroupData(firstGroupRequest.getId());


        Assert.assertEquals(healthDatasEX, result);
    }

    @Test
    public void groupRequestTest_birthdatemageq() {
        //create a mock users individuals
        String role = Role.ROLE_INDIVIDUAL.toString();
        int i = 0;
        Date birthDate = new Date(90, 01, 01);
        List<Individual> listIndividuals = new ArrayList<>();
        for (i = 0; i < 1001; i++) {
            String x = Integer.toString(i);
            User mockedUser = new User("username" + x, "password" + x, "aa@a" + x + "a.com", role);
            Individual mockedIndividual = new Individual();
            mockedIndividual.setUser(mockedUser);
            mockedIndividual.setFirstname("pippo" + x);
            mockedIndividual.setLastname("pippetti");
            int Ssn = 100000000;
            Ssn = Ssn + i;
            mockedIndividual.setBirthdate(birthDate);
            String z = Integer.toString(Ssn);
            mockedIndividual.setSsn(z);
            mockedIndividual.setAddress("via padova");
            listIndividuals.add(mockedIndividual);
        }
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate>=" + "19900201" + ";");
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);


        // TEST STARTS HERE

        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);


        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(listIndividuals);

        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest)).thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_SUCCESS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_heightInterval() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setHeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("height:" + 100 + "," + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_wheightInterval() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setWeight(100);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("weight:" + 100 + "," + 100);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }

    @Test
    public void groupRequestTestNotAnonymous_birthdateInterval() {
        //create a mock user individual
        String role = Role.ROLE_INDIVIDUAL.toString();
        Date birthDate = new Date(95, 01, 06);
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        mockedIndividual.setSsn("999999999");
        mockedIndividual.setBirthdate(birthDate);
        //create mock user thridparty
        String role2 = Role.ROLE_THIRD_PARTY.toString();
        User mockedUser2 = new User("Username", "Password", "AA@AA.com", role2);
        ThirdParty mockedThirdParty = new ThirdParty();
        mockedThirdParty.setUser(mockedUser2);
        mockedThirdParty.setVat("11111111111");
        mockedThirdParty.setOrganizationName("topolino");
        //create group requests
        GroupRequest firstGroupRequest = createMockGroupRequest("birthdate:" + birthDate + "," + birthDate);
        firstGroupRequest.setSubscription(false);
        //add request to a collection
        Collection<GroupRequest> groupRequests = new ArrayList<>();
        groupRequests.add(firstGroupRequest);
        //save it in thirdparty
        mockedThirdParty.setGroupRequests(groupRequests);
        List<Individual> indCol = new ArrayList<>();
        indCol.add(mockedIndividual);


        /* TEST STARTS HERE */
        mockThirdPartyAuthorized(mockedUser2, mockedThirdParty);

        Mockito.when(mockThirdPartyRepository.existsByUser(mockedUser2))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(mockedUser2))
                .thenReturn(mockedThirdParty);
        Mockito.when(mockIndividualRepository.existsBySsn(mockedIndividual.getSsn()))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findBySsn(mockedIndividual.getSsn()))
                .thenReturn(mockedIndividual);

        Mockito.when(mockIndividualRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(indCol);


        Mockito.when(mockGroupRequestRepository.save(firstGroupRequest))
                .thenReturn(firstGroupRequest);


        String result = requestService.groupRequest(firstGroupRequest);

        Assert.assertEquals(Message.REQUEST_NOT_ANONYMOUS.toString(), result);

    }


}