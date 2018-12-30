package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

import static org.junit.Assert.assertEquals;


public class AuthenticatorServiceTest {
    @InjectMocks
    private AuthenticatorService authenticatorService;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private IndividualRepository mockIndividualRepository;
    @Mock
    private ThirdPartyRepository mockThirdPartyRepository;
    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private Authentication mockAuthentication;
    @Mock
    private Principal mockPrincipal;

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

    @Test
    public void testIndividualRegistration() {
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        //mock "this user doesn't exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);

        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.REGISTRATION_SUCCESS.toString(), result);
    }


    @Test
    public void testThirdPartyRegistration() {
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Amazon");

        //mock "this user doesn't exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);

        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.REGISTRATION_SUCCESS.toString(), result);
    }

    @Test
    public void testIndividualRegistrationFail() {
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        //mock "this user ssn already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(true);

        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.INDIVIDUAL_ALREADY_EXISTS.toString(), result);
    }

    @Test
    public void testThirdPartyRegistrationFail(){
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Amazon");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);

        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.THIRD_PARTY_ALREADY_EXISTS.toString(), result);
    }


    @Test
    public void testIndividualLogin() {
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        Mockito.when(mockIndividualRepository.findByUser(dummyUser))
                .thenReturn(dummyIndividual);

        User result = authenticatorService.login(dummyUser);
        //todo how does login work?
        //assertEquals(dummyUser, result);
    }

    //todo add login failure for both individual and tp

    @Test
    public void testChangeIndividualProfile() {
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");
        //mock the updated individual
        Individual dummyIndividualUpdated = new Individual();
        dummyIndividualUpdated.setUser(dummyUser);
        dummyIndividualUpdated.setSsn("123456789");
        dummyIndividualUpdated.setFirstname("Changed Name!");
        dummyIndividualUpdated.setLastname("Pappo");
        //start test
        mockIndividualAuthorized(dummyUser, dummyIndividual);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeIndividualProfile(username, dummyIndividualUpdated);
        assertEquals(Message.CHANGE_PROFILE_SUCCESS.toString(), result);
    }

    @Test
    public void testChangeThirdPartyProfile() {
        User dummyUser = new User();
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing third party
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Amazon");
        //mock the updated third party
        ThirdParty dummyThirdPartyUpdated = new ThirdParty();
        dummyThirdPartyUpdated.setUser(dummyUser);
        dummyThirdPartyUpdated.setVat("12345678901");
        dummyThirdPartyUpdated.setOrganizationName("Changed Name!");
        //start test
        mockThirdPartyAuthorized(dummyUser, dummyThirdParty);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeThirdPartyProfile(username, dummyThirdPartyUpdated);
        assertEquals(Message.CHANGE_PROFILE_SUCCESS.toString(), result);
        //todo why the fuck this doesn't work. vat seems valid.
    }

}