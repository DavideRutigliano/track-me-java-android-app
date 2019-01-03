package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRequestRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


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





    @Before
    public void initTest(){
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
}