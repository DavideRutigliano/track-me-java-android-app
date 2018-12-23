package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.*;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
public class AuthenticatorController {

    @Autowired
    private AuthenticatorService authenticatorService;

    @MessageMapping("/testsocket")
    @SendTo("/topic/test")
    public String test(String message) {
        return message;
    } //TODO REMOVE THIS

    @PostMapping("/registration/individual")
    public String individualRegistration(@RequestBody IndividualRegistrationDTO individualRegistration) {

        ModelMapper modelMapper = new ModelMapper();

        User user = modelMapper.map(individualRegistration.getUser(), User.class);
        Individual individual = modelMapper.map(individualRegistration.getIndividual(), Individual.class);

        individual.setUser(user);

        return authenticatorService.individualRegistration(individual);
    }

    @PostMapping("/registration/thirdparty")
    public String thirdPartyRegistration(@RequestBody ThirdPartyRegistrationDTO thirdPartyRegistration) {

        ModelMapper modelMapper = new ModelMapper();

        User user = modelMapper.map(thirdPartyRegistration.getUser(), User.class);
        ThirdParty thirdParty = modelMapper.map(thirdPartyRegistration.getThirdParty(), ThirdParty.class);

        thirdParty.setUser(user);

        return authenticatorService.thirdPartyRegistration(thirdParty);
    }

    @PostMapping("/login")
    @ResponseBody
    public @DTO(UserDTO.class)
    User login(@RequestBody @DTO(UserDTO.class) User user,
               HttpServletResponse response) {

        User authenticated = authenticatorService.login(user);

        if (authenticated == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return authenticated;
    }

    @PreAuthorize("hasAnyRole('INDIVIDUAL','THIRD_PARTY')")
    @GetMapping("/logout")
    public String logout() {
        return "Success!";
    }

    @PreAuthorize("hasAnyRole('INDIVIDUAL','THIRD_PARTY')")
    @PutMapping("/{username}/username")
    public String changeUsername(@PathVariable("username") String username,
                                 @RequestBody String newUsername) {

        return authenticatorService.changeUsername(username, newUsername);
    }

    @PreAuthorize("hasAnyRole('INDIVIDUAL','THIRD_PARTY')")
    @PutMapping("/{username}/password")
    public String changePassword(@PathVariable("username") String username,
                                 @RequestBody String password) {

        return authenticatorService.changePassword(username, password);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping("/individual/data/{username}")
    @ResponseBody
    public @DTO(IndividualDTO.class)
    Individual getIndividualProfile(@PathVariable String username) {

        return authenticatorService.getIndividualProfile(username);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping("/individual/data/{username}")
    public String changeIndividualProfile(@PathVariable String username,
                                          @RequestBody @DTO(IndividualDTO.class) Individual individual) {

        return authenticatorService.changeIndividualProfile(username, individual);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping("/thirdparty/data/{username}")
    @ResponseBody
    public @DTO(ThirdPartyDTO.class)
    ThirdParty getThirdPartyProfile(@PathVariable String username) {

        return authenticatorService.getThirdPartyProfile(username);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @PutMapping("/thirdparty/data/{username}")
    public String changeThirdPartyProfile(@PathVariable String username,
                                          @RequestBody @DTO(ThirdPartyDTO.class) ThirdParty thirdParty) {

        return authenticatorService.changeThirdPartyProfile(username, thirdParty);
    }

}
