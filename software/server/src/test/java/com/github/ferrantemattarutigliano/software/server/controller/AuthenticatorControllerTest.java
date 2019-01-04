/*
package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AuthenticatorControllerTest {

    @InjectMocks
    AuthenticatorController mockAuthenticator;

    @Mock
    AuthenticatorService mockAuthenticatorService;
    @Mock
    IndividualRepository mockIndividualRepository;
    @Mock
    ThirdPartyRepository mockThirdPartyRepository;

    private UserDTO createDummyuserDTO() {

        return new UserDTO("jhon", "sort", "test@ho.com", "Individual");
    }

    private IndividualDTO createDummyIndivdualDTO() {
        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "102233444";
        return new IndividualDTO(ssn, "Jhon", "Snow", birthDate, 180, 70, "italia", "Bari", "via padova");
    }

    private IndividualRegistrationDTO createDummyIndivdualRegistrationDTO() {
        return new IndividualRegistrationDTO(createDummyuserDTO(), createDummyIndivdualDTO());

    }

    private ThirdParty createDummyThirdParty() {
        String vat = "00000000000";
        return new ThirdParty("Bilbo", "Baggins", vat, "test@ho.com", "A");
    }



    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void individualRegistrationTest() {
        IndividualRegistrationDTO dummyIndividual = createDummyIndivdualRegistrationDTO();

        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getIndividual().getSsn())).thenReturn(true);
        //    when(mockIndividualRepository.findBySsn(dummyIndividual.getIndividual().getSsn())).thenReturn(dummyIndividual);
        //    when(mockAuthenticatorService.individualRegistration(dummyIndividual)).thenReturn(true);

        String result = mockAuthenticator.individualRegistration(dummyIndividual);
        assertEquals("REGISTRATION_SUCCESS", result);
    }

    @Test
    public void testGetIndividualProfile() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(true);
        when(mockIndividualRepository.findBySsn(dummyIndividual.getSsn())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.individualRegistration(dummyIndividual)).thenReturn(true);

        Individual i = mockAuthenticator.getIndividualProfile(dummyIndividual.getUsername());
        assertEquals(dummyIndividual, i);
    }

    @Test
    public void thirdPartyRegistrationTest() {
        ThirdParty dummyThirdParty = createDummyThirdParty();

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        when(mockAuthenticatorService.thirdPartyRegistration((dummyThirdParty))).thenReturn(true);

        String result = mockAuthenticator.thirdPartyRegistration(dummyThirdParty);
        assertEquals("Success!", result);
    }

    @Test
    public void testGetThirdPartyProfile() {
        ThirdParty dummyThirdParty = createDummyThirdParty();

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        when(mockAuthenticatorService.getThirdPartyProfile(dummyThirdParty.getUsername())).thenReturn(dummyThirdParty);

        ThirdParty tp = mockAuthenticator.getThirdPartyProfile(dummyThirdParty.getUsername());
        assertEquals(dummyThirdParty, tp);
    }

    @Test
    public void individualLoginTest() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsByUsername(dummyIndividual.getUsername())).thenReturn(true);
        when(mockIndividualRepository.findByUsername(dummyIndividual.getUsername())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.login(dummyIndividual)).thenReturn(true);

        String result = mockAuthenticator.login(dummyIndividual);
        assertEquals("Welcome " + dummyIndividual.getUsername() + "!", result);
    }

    @Test
    public void testLoginFailure() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockAuthenticatorService.login(dummyIndividual)).thenReturn(false);

        String result = mockAuthenticator.login(dummyIndividual);
        assertEquals("Oops, something went wrong.", result);
    }

    @Test
    public void changeIndividualProfileTest() {
        Individual dummyIndividual = createDummyIndivdual();
        dummyIndividual.setEmail("you@know.nothing");

        when(mockIndividualRepository.existsByUsername(dummyIndividual.getUsername())).thenReturn(true);
        when(mockIndividualRepository.findByUsername(dummyIndividual.getUsername())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.changeIndividualProfile(dummyIndividual)).thenReturn(true);

        String result = mockAuthenticator.changeIndividualProfile(dummyIndividual.getUsername(), dummyIndividual);
        assertEquals("Success!", result);
    }

    @Test
    public void changeThirdPartyProfileTest() {
        ThirdParty dummyThirdParty = createDummyThirdParty();
        dummyThirdParty.setEmail("Bilbo@baggins.ring");

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        when(mockAuthenticatorService.changeThirdPartyProfile(dummyThirdParty)).thenReturn(true);

        String result = mockAuthenticator.changeThirdPartyProfile(dummyThirdParty.getUsername(), dummyThirdParty);
        assertEquals("Success!", result);
    }
}*/
