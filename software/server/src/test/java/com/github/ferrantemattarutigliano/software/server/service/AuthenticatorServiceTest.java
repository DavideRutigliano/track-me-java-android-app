package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AuthenticatorServiceTest {
    @InjectMocks
    AuthenticatorService authenticator;

    @Mock
    IndividualRepository mockIndividualRepository;
    @Mock
    ThirdPartyRepository mockThirdPartyRepository;

    private Individual createDummyIndivdual() {
        Date birthDate = new Date(Calendar.getInstance().getTimeInMillis());
        String ssn = "TESTER00D40V300A";
        return new Individual("Jhon", "Snow", ssn, "test@ho.com", "A", "B", birthDate);
    }

    private ThirdParty createDummyThirdParty() {
        String vat = "12345678901";
        return new ThirdParty("Bilbo", "Baggins", vat, "test@ho.com", "A");
    }

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndividualRegistration() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);

        boolean result = authenticator.individualRegistration(dummyIndividual);
        assertEquals(true, result);
    }

    @Test
    public void testThirdPartyRegistration() {
        ThirdParty dummyThirdParty = createDummyThirdParty();

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);

        boolean result = authenticator.thirdPartyRegistration(dummyThirdParty);
        assertEquals(true, result);
    }

    @Test
    public void testLogin() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsByUsername(dummyIndividual.getUsername())).thenReturn(true);
        when(mockIndividualRepository.findByUsername(dummyIndividual.getUsername())).thenReturn(dummyIndividual);

        boolean result = authenticator.login(dummyIndividual);
        assertEquals(true, result);
    }

    @Test
    public void testLoginFailure() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(true);
        when(mockIndividualRepository.findBySsn(dummyIndividual.getSsn())).thenReturn(dummyIndividual);

        boolean result = authenticator.login(dummyIndividual);
        assertEquals(false, result);
    }

    @Test
    public void testChangeIndividualProfile() {
        Individual dummyIndividual = createDummyIndivdual();

        when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(true);
        when(mockIndividualRepository.findBySsn(dummyIndividual.getSsn())).thenReturn(dummyIndividual);
        dummyIndividual.setLastname("Targaryen");
        dummyIndividual.setEmail("dany@love.com");

        boolean result = authenticator.changeIndividualProfile(dummyIndividual);
        assertEquals(true, result);
    }

    @Test
    public void testChangeThirdPartyProfile() {
        ThirdParty dummyThirdParty = createDummyThirdParty();

        when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);
        when(mockThirdPartyRepository.findByVat(dummyThirdParty.getVat())).thenReturn(dummyThirdParty);
        dummyThirdParty.setOrganizationName("Windown");
        dummyThirdParty.setEmail("bilbo@baggins.it");

        boolean result = authenticator.changeThirdPartyProfile(dummyThirdParty);
        assertEquals(true, result);
    }

}
