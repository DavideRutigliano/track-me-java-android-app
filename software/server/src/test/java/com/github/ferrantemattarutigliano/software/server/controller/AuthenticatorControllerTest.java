package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.dto.*;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;

public class AuthenticatorControllerTest {
    @InjectMocks
    private AuthenticatorController authenticatorController;

    @Mock
    private AuthenticatorService mockAuthenticatorService;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    public IndividualDTO createMockIndividualDTO() {
        //test constructor
        IndividualDTO individualDTO = new IndividualDTO("999999999", "pippo", "pippetti", new Date(1), 180, 100, "italy", "Rome", "via padova");
        //test setters and getters
        individualDTO.setSsn(individualDTO.getSsn());
        individualDTO.setFirstname(individualDTO.getFirstname());
        individualDTO.setLastname(individualDTO.getLastname());
        individualDTO.setBirthdate(individualDTO.getBirthdate());
        individualDTO.setHeight(individualDTO.getHeight());
        individualDTO.setWeight(individualDTO.getWeight());
        individualDTO.setState(individualDTO.getState());
        individualDTO.setCity(individualDTO.getCity());
        individualDTO.setAddress(individualDTO.getAddress());
        return individualDTO;
    }

    public ThirdPartyDTO createMockThirdPartyDTO() {
        //test constructor
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("99999999999", "topolino");
        //test setters and getters
        thirdPartyDTO.setVat(thirdPartyDTO.getVat());
        thirdPartyDTO.setOrganizationName(thirdPartyDTO.getOrganizationName());
        return thirdPartyDTO;
    }

    public UserDTO createMockUserIndDTO() {
        //test constructor
        UserDTO userDTO = new UserDTO("username", "password", "aa@aa");
        //test setters and getters
        userDTO.setUsername(userDTO.getUsername());
        userDTO.setPassword(userDTO.getPassword());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setRole(Role.ROLE_INDIVIDUAL.toString());
        return userDTO;
    }

    public UserDTO createMockUserTPDTO() {
        //test constructor
        UserDTO userDTO = new UserDTO("username", "password", "aa@aa", Role.ROLE_THIRD_PARTY.toString());
        //test setters and getters
        userDTO.setUsername(userDTO.getUsername());
        userDTO.setPassword(userDTO.getPassword());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setRole(userDTO.getRole());
        return userDTO;
    }

    public User convertUserDTO(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }

    public Individual convertIndividualDTO(IndividualDTO individualDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Individual individual = modelMapper.map(individualDTO, Individual.class);
        return individual;
    }

    public ThirdParty convertThirdPartyDTO(ThirdPartyDTO thirdPartyDTO) {
        ModelMapper modelMapper = new ModelMapper();
        ThirdParty thirdParty = modelMapper.map(thirdPartyDTO, ThirdParty.class);
        return thirdParty;
    }



    @Test
    public void individualRegistrationTest() {

        //create mocked user DTO

        UserDTO mockedUserDTO = createMockUserIndDTO();

        //create mocked Individual DTO

        IndividualDTO mockedindividualDTO = createMockIndividualDTO();
        IndividualRegistrationDTO individualRegistrationDTO = new IndividualRegistrationDTO(mockedUserDTO, mockedindividualDTO);

        //test Individual Registration DTO

        UserDTO a = individualRegistrationDTO.getUser();
        IndividualDTO b = individualRegistrationDTO.getIndividual();

        /* TEST STARTS HERE */

        Mockito.when(mockAuthenticatorService.individualRegistration(any(Individual.class)))
                .thenReturn(Message.REGISTRATION_SUCCESS.toString());

        String result = authenticatorController.individualRegistration(individualRegistrationDTO);

        Assert.assertEquals(Message.REGISTRATION_SUCCESS.toString(), result);
    }

    @Test
    public void thirdPartyRegistrationTest() {

        //create mocked user DTO

        UserDTO mockedUserDTO = createMockUserTPDTO();

        //create mocked ThirdParty DTO

        ThirdPartyDTO mockedThirdPartyDTO = createMockThirdPartyDTO();
        ThirdPartyRegistrationDTO thirdPartyRegistrationDTO = new ThirdPartyRegistrationDTO(mockedUserDTO, mockedThirdPartyDTO);

        //test ThirdParty Registration DTO

        UserDTO a = thirdPartyRegistrationDTO.getUser();
        ThirdPartyDTO b = thirdPartyRegistrationDTO.getThirdParty();

        /* TEST STARTS HERE */

        Mockito.when(mockAuthenticatorService.thirdPartyRegistration(any(ThirdParty.class)))
                .thenReturn(Message.REGISTRATION_SUCCESS.toString());

        String result = authenticatorController.thirdPartyRegistration(thirdPartyRegistrationDTO);

        Assert.assertEquals(Message.REGISTRATION_SUCCESS.toString(), result);
    }

    @Test
    public void loginTest() {
        //create mocked user DTO

        UserDTO mockedUserDTO = createMockUserTPDTO();

        //convert it into a user

        User mockedUser = convertUserDTO(mockedUserDTO);

        /* TEST STARTS HERE */
        Mockito.when(mockAuthenticatorService.login(mockedUser))
                .thenReturn(mockedUser);

        User result = authenticatorController.login(mockedUser, any(HttpServletResponse.class));

        Assert.assertEquals(mockedUser, result);

    }

    @Test
    public void loginTest_Unauthorized() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        //create mocked user DTO
        UserDTO mockedUserDTO = createMockUserTPDTO();

        //convert it into a user

        User mockedUser = convertUserDTO(mockedUserDTO);

        /* TEST STARTS HERE */
        Mockito.when(mockAuthenticatorService.login(mockedUser))
                .thenReturn(null);

        authenticatorController.login(mockedUser, response);

        Assert.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void logoutTest() {
        String result = authenticatorController.logout();

        Assert.assertEquals("Success!", result);

    }

    @Test
    public void changeUsernameTest() {
        Mockito.when(mockAuthenticatorService.changeUsername("username", "username2"))
                .thenReturn(Message.CHANGE_USERNAME_SUCCESS.toString());

        String result = authenticatorController.changeUsername("username", "username2");

        Assert.assertEquals(Message.CHANGE_USERNAME_SUCCESS.toString(), result);

    }

    @Test
    public void changePasswordTest() {
        Mockito.when(mockAuthenticatorService.changePassword("username", "username2"))
                .thenReturn(Message.CHANGE_PASSWORD_SUCCESS.toString());

        String result = authenticatorController.changePassword("username", "username2");

        Assert.assertEquals(Message.CHANGE_PASSWORD_SUCCESS.toString(), result);

    }

    @Test
    public void getIndividualProfileTest() {

        //create dummy user

        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        //create dummy individual

        Individual dummyIndividual = new Individual();
        dummyIndividual.setUser(dummyUser);
        dummyIndividual.setSsn("123456789");
        dummyIndividual.setFirstname("Pippo");
        dummyIndividual.setLastname("Pappo");

        /* TEST STARTS HERE */

        Mockito.when(mockAuthenticatorService.getIndividualProfile("username"))
                .thenReturn(dummyIndividual);

        Individual result = authenticatorController.getIndividualProfile("username");

        Assert.assertEquals(dummyIndividual, result);


    }

    @Test
    public void changeIndividualProfileTest() {

        //create dummy user

        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        //create dummy individual

        IndividualDTO individualDTO = createMockIndividualDTO();
        Individual dummyIndividual = convertIndividualDTO(individualDTO);
        dummyIndividual.setUser(dummyUser);


        /* TEST STARTS HERE */

        Mockito.when(mockAuthenticatorService.changeIndividualProfile("username", dummyIndividual))
                .thenReturn(Message.CHANGE_PROFILE_SUCCESS.toString());

        String result = authenticatorController.changeIndividualProfile("username", dummyIndividual);

        Assert.assertEquals(Message.CHANGE_PROFILE_SUCCESS.toString(), result);


    }

    @Test
    public void getThirdPartyProfileTest() {

        //create dummy user

        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        //create dummy thirdParty

        //create mock ThirdParty
        ThirdParty dummyThirdParty = new ThirdParty();
        dummyThirdParty.setUser(dummyUser);
        dummyThirdParty.setVat("12345678901");
        dummyThirdParty.setOrganizationName("Windown");

        /* TEST STARTS HERE */

        Mockito.when(mockAuthenticatorService.getThirdPartyProfile("username"))
                .thenReturn(dummyThirdParty);

        ThirdParty result = authenticatorController.getThirdPartyProfile("username");

        Assert.assertEquals(dummyThirdParty, result);


    }

    @Test
    public void changeThirdPartyProfileTest() {

        //create dummy user

        User dummyUser = new User("username", "password", "aa@aa.com", "individual");
        dummyUser.setUsername("username");
        dummyUser.setPassword("password");
        dummyUser.setEmail("email@email.com");

        //create dummy individual

        ThirdPartyDTO thirdPartyDTO = createMockThirdPartyDTO();
        ThirdParty dummyThirdParty = convertThirdPartyDTO(thirdPartyDTO);
        dummyThirdParty.setUser(dummyUser);


        /* TEST STARTS HERE */

        Mockito.when(mockAuthenticatorService.changeThirdPartyProfile("username", dummyThirdParty))
                .thenReturn(Message.CHANGE_PROFILE_SUCCESS.toString());

        String result = authenticatorController.changeThirdPartyProfile("username", dummyThirdParty);

        Assert.assertEquals(Message.CHANGE_PROFILE_SUCCESS.toString(), result);


    }
}