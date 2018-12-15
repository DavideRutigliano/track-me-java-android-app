package com.github.ferrantemattarutigliano.software.server.service;
/*
import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRequestRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
public class RequestServiceTest {
    @InjectMocks
    RequestService requestService;

    @Mock
    IndividualRepository mockIndividualRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    ThirdPartyRepository mockThirdPartyRepository;

    @Mock
    IndividualRequestRepository mockIndividualRequestRepository;

    @Mock
    Authentication mockAuthentication;

    @Before
    public void initTest(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndividualRequest(){
        mockStatic(SecurityContextHolder.class);
        expect(SecurityContextHolder.getContext().getAuthentication()).willReturn(mockAuthentication);

        User dummySenderUser = new User("amazon", "1234", "ama@zon.com");
        ThirdParty dummyThirdParty = new ThirdParty("ABCD1111111", "Amazon s.r.l");

        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "12345678901";
        User dummyReceiverUser = new User("pippo", "1234", "auu@ken.com");
        Individual dummyIndividual = new Individual(dummyReceiverUser, ssn, "Pippo", "Pippo", birthDate);

        //mock authentication
        when(mockAuthentication.getPrincipal()).thenReturn(dummySenderUser);
        //mock third party
        when(mockThirdPartyRepository.existsByUser(dummySenderUser)).thenReturn(true);
        when(mockThirdPartyRepository.findByUser(dummySenderUser)).thenReturn(dummyThirdParty);
        //mock individual
        when(mockIndividualRepository.existsByUser(dummyReceiverUser)).thenReturn(true);
        when(mockIndividualRepository.findBySsn(ssn)).thenReturn(dummyIndividual);

        IndividualRequest dummyRequest = new IndividualRequest("12345678901");
        String result = requestService.individualRequest(dummyRequest);

        assertEquals(Message.REQUEST_SUCCESS.toString(), result);
    }

    @Test
    public void testInvalidIndividualRequest(){
        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "12345678901";

        User dummyUser = new User("pippo", "1234", "auu@ken.com");
        Individual dummyIndividual = new Individual(dummyUser, ssn, "Pippo", "Pippo", birthDate);
        when(mockIndividualRepository.existsBySsn(ssn)).thenReturn(true);
        when(mockIndividualRepository.findBySsn(ssn)).thenReturn(dummyIndividual);

        IndividualRequest dummyRequest = new IndividualRequest("invalid");
        String result = requestService.individualRequest(dummyRequest);

        assertEquals(Message.REQUEST_INVALID_SSN.toString(), result);
    }

    @Test
    public void testShowSentIndividualRequest(){
        IndividualRequest r1 = new IndividualRequest();
    }
} */