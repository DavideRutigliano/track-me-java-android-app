package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.*;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    @Mock
    private DaoAuthenticationProvider mockAuthenticationProvider;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

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
        Mockito.when(mockIndividualRepository.existsByUser(expectedUser))
                .thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByUser(expectedUser))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(expectedUser))
                .thenReturn(expectedThirdParty);
    }

    @Test
    public void individualRegistrationTest() {
        //create dummy user
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        //for only  test user class purpose
        Boolean a = dummyUser.isAccountNonExpired();
        Boolean b = dummyUser.isAccountNonLocked();
        Boolean c = dummyUser.isCredentialsNonExpired();
        Boolean d = dummyUser.isEnabled();
        Long e = dummyUser.getId();

        //create dummy individual
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        //for only test individual class purpose
        dummyIndividual.setId(0L);
        Long f = dummyIndividual.getId();

        //mock "this user doesn't exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);

        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.REGISTRATION_SUCCESS.toString(), result);
    }

    @Test
    public void individualRegistrationFail_BadSsn_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
    public void individualRegistrationFail_BadEmail_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(true);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);

        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.INDIVIDUAL_ALREADY_EXISTS.toString(), result);
    }

    @Test
    public void individualRegistrationFail_BadUsername_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        //mock "this user ssn already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(true);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);

        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.INDIVIDUAL_ALREADY_EXISTS.toString(), result);
    }

    @Test
    public void individualRegistrationFail_InvalidEmail_Test(){
        User dummyUser = new User("username", "password", "aa:aa_com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);


        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.INVALID_EMAIL.toString(), result);
    }

    @Test
    public void individualRegistrationFail_InvalidSsn_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("not valid");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockIndividualRepository.existsBySsn(dummyIndividual.getSsn())).thenReturn(false);

        String result = authenticatorService.individualRegistration(dummyIndividual);
        assertEquals(Message.BAD_PARAMETERS.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationTest() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        //create mock ThirdParty
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Windown");

        //the following is only to test ThirdParty class
        Collection<IndividualRequest> j = new ArrayList<>();
        j = dummyThirdParty.getIndividualRequests();
        Collection<GroupRequest> k = new ArrayList<>();
        k = dummyThirdParty.getGroupRequests();
        dummyThirdParty.setId(0L);
        Long l = dummyThirdParty.getId();

        //mock "this user doesn't exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);

        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.REGISTRATION_SUCCESS.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationFail_BadVat_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Windown");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(true);

        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.THIRD_PARTY_ALREADY_EXISTS.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationFail_BadEmail_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Windown");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(true);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);

        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.THIRD_PARTY_ALREADY_EXISTS.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationFail_BadUsername_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Windown");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(true);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);

        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.THIRD_PARTY_ALREADY_EXISTS.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationFail_InvalidEmail_Test(){
        User dummyUser = new User("username", "password", "aa:aa_com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Windown");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);


        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.INVALID_EMAIL.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationFail_InvalidVat_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");

        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("invalid");
        dummyThirdParty.setOrganizationName("Windown");

        //mock "this user vat already exists"
        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername())).thenReturn(false);
        Mockito.when(mockUserRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByVat(dummyThirdParty.getVat())).thenReturn(false);


        String result = authenticatorService.thirdPartyRegistration(dummyThirdParty);
        assertEquals(Message.BAD_PARAMETERS.toString(), result);
    }

    @Test
    public void individualLoginTest() {
        SecurityContextHolder.setContext(mockSecurityContext);
        User dummyUser = new User("username", "password", "aa@aa.com", "INDIVIDUAL");
        dummyUser.setUsername("username");
        String encoded = new BCryptPasswordEncoder(12).encode("password");
        Mockito.when(mockPasswordEncoder.encode("password")).thenReturn(encoded);
        dummyUser.setPassword(encoded);
        dummyUser.setEmail("email@email.com");
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername()))
                .thenReturn(true);

        Mockito.when(mockUserRepository.findByUsername(dummyUser.getUsername()))
                .thenReturn(dummyUser);

        Mockito.when(mockIndividualRepository.existsByUser(dummyUser))
                .thenReturn(true);

        Mockito.when(mockIndividualRepository.findByUser(dummyUser))
                .thenReturn(dummyIndividual);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dummyUser.getUsername(), dummyUser.getPassword());

        Mockito.when(mockAuthenticationProvider.authenticate(token)).thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(dummyUser);

        User result = authenticatorService.login(dummyUser);

        assertEquals(dummyUser , result);
    }

    @Test
    public void thirdPartyLoginTest() {
        SecurityContextHolder.setContext(mockSecurityContext);
        User dummyUser = new User("username", "password", "aa@aa.com", "THIRD_PARTY");
        dummyUser.setUsername("username");
        String encoded = new BCryptPasswordEncoder(12).encode("password");
        Mockito.when(mockPasswordEncoder.encode("password")).thenReturn(encoded);
        dummyUser.setPassword(encoded);
        dummyUser.setEmail("email@email.com");
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678900");
        dummyThirdParty.setOrganizationName("Pippo");


        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername()))
                .thenReturn(true);

        Mockito.when(mockUserRepository.findByUsername(dummyUser.getUsername()))
                .thenReturn(dummyUser);

        Mockito.when(mockThirdPartyRepository.existsByUser(dummyUser))
                .thenReturn(true);

        Mockito.when(mockThirdPartyRepository.findByUser(dummyUser))
                .thenReturn(dummyThirdParty);

        SecurityContextHolder.setContext(mockSecurityContext);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dummyUser.getUsername(), dummyUser.getPassword());

        Mockito.when(mockAuthenticationProvider.authenticate(token)).thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(dummyUser);

        User result = authenticatorService.login(dummyUser);

        assertEquals(dummyUser , result);
    }

    @Test
    public void individualLoginFailureTest() {
        SecurityContextHolder.setContext(mockSecurityContext);
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        String encoded = new BCryptPasswordEncoder(12).encode("password");
        Mockito.when(mockPasswordEncoder.encode("password")).thenReturn(encoded);
        dummyUser.setPassword(encoded);
        dummyUser.setEmail("email@email.com");
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername()))
                .thenReturn(true);

        Mockito.when(mockUserRepository.findByUsername(dummyUser.getUsername()))
                .thenReturn(dummyUser);

        Mockito.when(mockIndividualRepository.existsByUser(dummyUser))
                .thenReturn(true);

        Mockito.when(mockIndividualRepository.findByUser(dummyUser))
                .thenReturn(dummyIndividual);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dummyUser.getUsername(), dummyUser.getPassword());

        Mockito.when(mockAuthenticationProvider.authenticate(token)).thenThrow(BadCredentialsException.class);

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(null);

        User result = authenticatorService.login(dummyUser);

        assertNull(result);
    }

    @Test
    public void thirdPartyLoginFailureTest() {
        SecurityContextHolder.setContext(mockSecurityContext);
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        String encoded = new BCryptPasswordEncoder(12).encode("password");
        Mockito.when(mockPasswordEncoder.encode("password")).thenReturn(encoded);
        dummyUser.setPassword(encoded);
        dummyUser.setEmail("email@email.com");
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678900");
        dummyThirdParty.setOrganizationName("Pippo");


        Mockito.when(mockUserRepository.existsByUsername(dummyUser.getUsername()))
                .thenReturn(true);

        Mockito.when(mockUserRepository.findByUsername(dummyUser.getUsername()))
                .thenReturn(dummyUser);

        Mockito.when(mockThirdPartyRepository.existsByUser(dummyUser))
                .thenReturn(true);

        Mockito.when(mockThirdPartyRepository.findByUser(dummyUser))
                .thenReturn(dummyThirdParty);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dummyUser.getUsername(), dummyUser.getPassword());

        Mockito.when(mockAuthenticationProvider.authenticate(token)).thenThrow(BadCredentialsException.class);

        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(null);

        User result = authenticatorService.login(dummyUser);

        assertNull(result);
    }

    @Test
    public void changeIndividualProfileTest() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
        dummyIndividualUpdated.setFirstname("Changed Name!");
        dummyIndividualUpdated.setLastname("Pappo");
        dummyIndividualUpdated.setWeight(200);
        dummyIndividualUpdated.setHeight(170);
        dummyIndividualUpdated.setCity("Milan");
        dummyIndividualUpdated.setState("Italy");
        dummyIndividualUpdated.setAddress("Via roma 123");
        //start test
        mockIndividualAuthorized(dummyUser, dummyIndividual);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeIndividualProfile(username, dummyIndividualUpdated);
        assertEquals(Message.CHANGE_PROFILE_SUCCESS.toString(), result);
    }

    @Test
    public void changeIndividualProfile_Unauthenticated_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
        dummyIndividualUpdated.setFirstname("Changed Name!");
        dummyIndividualUpdated.setLastname("Pappo");
        dummyIndividualUpdated.setWeight(200);
        dummyIndividualUpdated.setHeight(170);
        dummyIndividualUpdated.setCity("Milan");
        dummyIndividualUpdated.setState("Italy");
        dummyIndividualUpdated.setAddress("Via roma 123");
        //start test
        SecurityContextHolder.setContext(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(null);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeIndividualProfile(username, dummyIndividualUpdated);
        assertEquals(Message.BAD_LOGIN.toString(), result);
    }

    @Test
    public void changeIndividualProfile_BadParameters_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
        dummyIndividualUpdated.setFirstname("Changed Name!");
        dummyIndividualUpdated.setLastname("Pappo");
        dummyIndividualUpdated.setWeight(-1);
        dummyIndividualUpdated.setHeight(-1);
        dummyIndividualUpdated.setCity("Milan");
        dummyIndividualUpdated.setState("Italy");
        dummyIndividualUpdated.setAddress("Via roma 123");
        //start test
        mockIndividualAuthorized(dummyUser, dummyIndividual);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeIndividualProfile(username, dummyIndividualUpdated);
        assertEquals(Message.BAD_PARAMETERS.toString(), result);
    }

    @Test
    public void changeThirdPartyProfileTest() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
    }

    @Test
    public void changeThirdPartyProfile_Unauthenticated_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
        dummyThirdPartyUpdated.setVat("hello");
        dummyThirdPartyUpdated.setOrganizationName("Change nothing :D");
        //start test
        SecurityContextHolder.setContext(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(null);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeThirdPartyProfile(username, dummyThirdPartyUpdated);
        assertEquals(Message.BAD_REQUEST.toString(), result);
    }

    @Test
    public void changeThirdPartyProfile_BadParameters_Test() {
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
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
        dummyThirdPartyUpdated.setVat("hello");
        dummyThirdPartyUpdated.setOrganizationName("Change nothing :D");
        //start test
        mockThirdPartyAuthorized(dummyUser, dummyThirdParty);

        String username = dummyUser.getUsername();
        String result = authenticatorService.changeThirdPartyProfile(username, dummyThirdPartyUpdated);
        assertEquals(Message.BAD_PARAMETERS.toString(), result);
    }

    @Test
    public void getThirdPartyProfileTest(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing third party
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Amazon");

        mockThirdPartyAuthorized(dummyUser, dummyThirdParty);
        ThirdParty result = authenticatorService.getThirdPartyProfile("username");
        Assert.assertEquals(dummyThirdParty, result);
    }

    @Test
    public void getIndividualProfileTest(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        mockIndividualAuthorized(dummyUser, dummyIndividual);
        Individual result = authenticatorService.getIndividualProfile("username");
        Assert.assertEquals(dummyIndividual, result);
    }

    @Test
    public void changeIndividualUsernameTest(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        mockIndividualAuthorized(dummyUser, dummyIndividual);
        String result = authenticatorService.changeUsername("username", "NEW");
        String expected = Message.CHANGE_USERNAME_SUCCESS.toString() + "NEW";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void changeIndividualUsername_BadUsername_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        mockIndividualAuthorized(dummyUser, dummyIndividual);

        String alreadyExisting = "alreadyExisting";
        Mockito.when(mockUserRepository.existsByUsername(alreadyExisting)).thenReturn(true);

        String result = authenticatorService.changeUsername("username", alreadyExisting);
        String expected = Message.USERNAME_ALREADY_EXISTS.toString() + alreadyExisting;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void changeThirdPartyUsernameTest(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678999");
        dummyThirdParty.setOrganizationName("Pippo");

        mockThirdPartyAuthorized(dummyUser, dummyThirdParty);
        String result = authenticatorService.changeUsername("username", "NEW");
        String expected = Message.CHANGE_USERNAME_SUCCESS.toString() + "NEW";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void changeThirdPartyUsername_BadUsername_Test(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678999");
        dummyThirdParty.setOrganizationName("Pippo");

        mockThirdPartyAuthorized(dummyUser, dummyThirdParty);

        String alreadyExisting = "alreadyExisting";
        Mockito.when(mockUserRepository.existsByUsername(alreadyExisting)).thenReturn(true);

        String result = authenticatorService.changeUsername("username", alreadyExisting);
        String expected = Message.USERNAME_ALREADY_EXISTS.toString() + alreadyExisting;
        Assert.assertEquals(expected, result);

        Assert.assertEquals(expected, result);
    }


    @Test
    public void changePasswordTest(){
        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");
        //mock the existing individual
        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        mockIndividualAuthorized(dummyUser, dummyIndividual);
        String result = authenticatorService.changePassword("username", "HELLO");
        Assert.assertEquals(Message.CHANGE_PASSWORD_SUCCESS.toString(), result);
    }

    @Test
    public void loadIndividualByUsernameTest() {
        User dummyUser = Mockito.spy(User.class);
        String username = "username";
        String password = "password";
        String email = "hue@alpha.com";
        dummyUser.setUsername(username);
        dummyUser.setPassword(password);
        dummyUser.setEmail(email);
        //user exists in database
        Mockito.when(mockUserRepository.existsByUsername(username))
                .thenReturn(true);
        Mockito.when(mockUserRepository.findByUsername(username))
                .thenReturn(dummyUser);
        //let's suppose that that user is an Individual
        Mockito.when(mockIndividualRepository.existsByUser(dummyUser))
                .thenReturn(true);

        UserDetails result = authenticatorService.loadUserByUsername(username);

        Assert.assertEquals(Role.ROLE_INDIVIDUAL.toString(), dummyUser.getRole());
        Assert.assertSame(dummyUser, result);
    }

    @Test
    public void loadThirdPartyByUsernameTest() {
        User dummyUser = Mockito.spy(User.class);
        String username = "username";
        String password = "password";
        String email = "hue@alpha.com";
        dummyUser.setUsername(username);
        dummyUser.setPassword(password);
        dummyUser.setEmail(email);
        //user exists in database
        Mockito.when(mockUserRepository.existsByUsername(username))
                .thenReturn(true);
        Mockito.when(mockUserRepository.findByUsername(username))
                .thenReturn(dummyUser);
        //let's suppose that that user is a Third Party
        Mockito.when(mockIndividualRepository.existsByUser(dummyUser))
                .thenReturn(false);
        Mockito.when(mockThirdPartyRepository.existsByUser(dummyUser))
                .thenReturn(true);

        UserDetails result = authenticatorService.loadUserByUsername(username);

        Assert.assertEquals(Role.ROLE_THIRD_PARTY.toString(), dummyUser.getRole());
        Assert.assertSame(dummyUser, result);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsernameTest_Null_UsernameNotFound() {
        authenticatorService.loadUserByUsername(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsernameTest_Empty_UsernameNotFound() {
        authenticatorService.loadUserByUsername("");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsernameTest_UsernameNotFound() {
        User dummyUser = Mockito.spy(User.class);
        String username = "username";
        String password = "password";
        String email = "hue@alpha.com";
        dummyUser.setUsername(username);
        dummyUser.setPassword(password);
        dummyUser.setEmail(email);
        //user exists in database
        Mockito.when(mockUserRepository.existsByUsername(username))
                .thenReturn(false);
        Mockito.when(mockUserRepository.findByUsername(username))
                .thenReturn(null);
        //let's suppose that that user is a Third Party
        Mockito.when(mockThirdPartyRepository.existsByUser(dummyUser))
                .thenReturn(false);

        authenticatorService.loadUserByUsername("bad username");
    }

}