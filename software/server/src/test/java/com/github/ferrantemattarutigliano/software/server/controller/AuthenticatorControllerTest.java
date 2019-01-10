package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;

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

}