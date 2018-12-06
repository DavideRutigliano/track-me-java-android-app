package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class AuthenticatorController {

    @Autowired
    private AuthenticatorService authenticatorService;

    @PostMapping("/registration/individual")
    public String individualRegistration(@RequestBody @DTO(IndividualDTO.class) Individual individual) {

        if (authenticatorService.individualRegistration(individual))
            return "Success!";
        else return "Oops, something went wrong.";
    }

    @PostMapping("/registration/thirdparty")
    public String thirdPartyRegistration(@RequestBody @DTO(ThirdPartyDTO.class) ThirdParty thirdParty) {

        if (authenticatorService.thirdPartyRegistration(thirdParty))
            return "Success!";
        else return "Oops, something went wrong.";
    }

    @PostMapping("/login")
    @ResponseBody
    public @DTO(UserDTO.class)
    User login(@RequestBody @DTO(UserDTO.class) User user) {

        return authenticatorService.login(user);
    }

    @GetMapping("/logout")
    public String logout() {
        return "Success!";
    } //TODO delete "session"

    @GetMapping("/individuals/{username}")
    @ResponseBody
    public @DTO(IndividualDTO.class)
    Individual getIndividualProfile(@PathVariable String username) {

        return authenticatorService.getIndividualProfile(username);
    }

    @PutMapping("/individuals/{username}")
    public String changeIndividualProfile(@PathVariable String username,
                                          @RequestBody @DTO(IndividualDTO.class) Individual individual) {

        if (authenticatorService.changeIndividualProfile(individual))
            return "Success!";
        else return "Oops, user " + individual.getUsername() + " does not exists.";
    }

    @GetMapping("/thirdparties/{username}")
    @ResponseBody
    public @DTO(ThirdPartyDTO.class)
    ThirdParty getThirdPartyProfile(@PathVariable String username) {

        return authenticatorService.getThirdPartyProfile(username);
    }

    @PutMapping("/thirdparties/{username}")
    public String changeThirdPartyProfile(@PathVariable String username,
                                          @RequestBody @DTO(ThirdPartyDTO.class) ThirdParty thirdParty) {

        if (authenticatorService.changeThirdPartyProfile(thirdParty))
            return "Success!";
        else return "Oops, user " + thirdParty.getUsername() + " does not exists.";
    }

    @PutMapping("/changeusername/{username}")
    public String changeUsername(@PathVariable("username") String username,
                                 @RequestBody @DTO(UserDTO.class) User user) {

        if (authenticatorService.changeUsername(user, username))
            return "Success!";
        else return "Oops, something went wrong";
    }

    @PutMapping("/changepassword/{username}")
    public String changePassword(@PathVariable("username") String username,
                                 @RequestBody @DTO(UserDTO.class) User user,
                                 String password) {

        if (authenticatorService.changePassword(user, password))
            return "Success!";
        else return "Oops, something went wrong";
    }

}
