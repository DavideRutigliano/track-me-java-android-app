package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticatorControllerTest {

    @InjectMocks
    AuthenticatorController mockAuthenticator;

    @Mock
    AuthenticatorService mockAuthenticatorService;
    @Mock
    IndividualRepository mockIndividualRepository;
    @Mock
    ThirdPartyRepository mockThirdPartyRepository;

    private Individual createDummyIndivdual() {
        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "AAAAAA00A00A000A";
        return new Individual("Jhon", "Snow", ssn, "test@ho.com", "A", "B", birthDate);
    }

    private ThirdParty createDummyThirdParty() {
        String vat = "AAAAAA00A00A000A";
        return new ThirdParty("Bilbo", "Baggins", vat, "test@ho.com", "A");
    }

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndividualRegistration() throws Exception {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(true);
        when(mockIndividualRepository.findBySsn(dummyIndividual.getSsn())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.individualRegistration(dummyIndividual)).thenReturn(true);

        String result = mockAuthenticator.individualRegistration(dummyIndividual);
        assertEquals("Success!", result);
    }

    @Test
    public void testGetIndividualProfile() throws Exception {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(true);
        when(mockIndividualRepository.findBySsn(dummyIndividual.getSsn())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.individualRegistration(dummyIndividual)).thenReturn(true);

        Individual i = mockAuthenticator.getIndividualProfile(dummyIndividual.getUsername());
        assertEquals(dummyIndividual, i);
    }

    @Test
    public void testThirdPartyRegistration() throws Exception {
        ThirdParty dummyThirdParty = createDummyThirdParty();

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        when(mockAuthenticatorService.thirdPartyRegistration((dummyThirdParty))).thenReturn(true);

        String result = mockAuthenticator.thirdPartyRegistration(dummyThirdParty);
        assertEquals("Success!", result);
    }

    @Test
    public void testGetThirdPartyProfile() throws Exception {
        ThirdParty dummyThirdParty = createDummyThirdParty();

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        when(mockAuthenticatorService.getThirdPartyProfile(dummyThirdParty.getUsername())).thenReturn(dummyThirdParty);

        ThirdParty tp = mockAuthenticator.getThirdPartyProfile(dummyThirdParty.getUsername());
        assertEquals(dummyThirdParty, tp);
    }

    @Test
    public void testLogin() throws Exception {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsByUsername(dummyIndividual.getUsername())).thenReturn(true);
        when(mockIndividualRepository.findByUsername(dummyIndividual.getUsername())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.login(dummyIndividual)).thenReturn(true);

        String result = mockAuthenticator.login(dummyIndividual);
        assertEquals("Welcome " + dummyIndividual.getUsername() + "!", result);
    }

    @Test
    public void testLoginFailure() throws Exception {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockAuthenticatorService.login(dummyIndividual)).thenReturn(false);

        String result = mockAuthenticator.login(dummyIndividual);
        assertEquals("Oops, something went wrong.", result);
    }

    @Test
    public void testChangeIndividualProfile() {
        Individual dummyIndividual = createDummyIndivdual();
        dummyIndividual.setEmail("you@know.nothing");

        when(mockIndividualRepository.existsByUsername(dummyIndividual.getUsername())).thenReturn(true);
        when(mockIndividualRepository.findByUsername(dummyIndividual.getUsername())).thenReturn(dummyIndividual);
        when(mockAuthenticatorService.changeIndividualProfile(dummyIndividual)).thenReturn(true);

        String result = mockAuthenticator.changeIndividualProfile(dummyIndividual.getUsername(), dummyIndividual);
        assertEquals("Success!", result);
    }

    @Test
    public void testChangeThirdPartyProfile() {
        ThirdParty dummyThirdParty = createDummyThirdParty();
        dummyThirdParty.setEmail("Bilbo@baggins.ring");

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        when(mockAuthenticatorService.changeThirdPartyProfile(dummyThirdParty)).thenReturn(true);

        String result = mockAuthenticator.changeThirdPartyProfile(dummyThirdParty.getUsername(), dummyThirdParty);
        assertEquals("Success!", result);
    }
}
