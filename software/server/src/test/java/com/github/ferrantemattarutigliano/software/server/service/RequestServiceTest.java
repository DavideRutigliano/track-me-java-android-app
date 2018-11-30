package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.message.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RequestServiceTest {
    @InjectMocks
    RequestService requestService;

    @Mock
    IndividualRepository mockIndividualRepository;

    @Mock
    IndividualRequestRepository mockIndividualRequestRepository;

    @Before
    public void initTest(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndividualRequest(){
        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "123456789";

        Individual dummyIndividual = new Individual(ssn, "test@ho.com", "A", "B", birthDate);
        when(mockIndividualRepository.existsBySsn(ssn)).thenReturn(true);
        when(mockIndividualRepository.findBySsn(ssn)).thenReturn(dummyIndividual);

        IndividualRequest dummyRequest = new IndividualRequest("123456789");
        String result = requestService.individualRequest(dummyRequest);

        assertEquals(Message.REQUEST_SUCCESS.toString(), result);
    }

    @Test
    public void testInvalidIndividualRequest(){
        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "123456789";

        Individual dummyIndividual = new Individual(ssn, "test@ho.com", "A", "B", birthDate);
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
}