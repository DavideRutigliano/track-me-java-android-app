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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class AuthenticatorController {

    @Autowired
    private AuthenticatorService authenticatorService;

    @GetMapping("/individuals")
    public Collection<Individual> getAllIndividuals() {
        return authenticatorService.getAllIndividuals();
    }

    @PostMapping("/individuals/add")
    public String individualRegistration(@RequestBody @DTO(IndividualDTO.class) Individual individual) {
        if (authenticatorService.individualRegistration(individual))
            return "Success!";
        else return "Oops, something went wrong.";
    }

    @GetMapping("/thirdparties")
    public Collection<ThirdParty> getAllThirdParties() { return authenticatorService.getAllThirdParties(); }

    @PostMapping("/thirdparties/add")
    public String thirdPartyRegistration(@RequestBody @DTO(ThirdPartyDTO.class) ThirdParty thirdParty) {
        if (authenticatorService.thirdPartyRegistration(thirdParty))
            return "Success!";
        else return "Oops, something went wrong.";
    }

    @PostMapping("/login")
    public String login(@RequestBody @DTO(UserDTO.class) User user) {
        if (authenticatorService.login(user))
            return "Welcome "+user.getUsername()+"!";
        else return "Oops, something went wrong.";
    }

    @GetMapping("/individuals/{username}")
    public Individual getIndividualProfile(@PathVariable String username) {
        return authenticatorService.getIndividualProfile(username);
    }

    @PutMapping("/individuals/{username}")
    public String changeIndividualProfile(@PathVariable String username, @DTO(IndividualDTO.class) Individual individual) {
        if (authenticatorService.changeIndividualProfile(individual))
            return "Success!";
        else return "Oops, user "+individual.getUsername()+" does not exists";
    }

    @GetMapping("/thirdparties/{username}")
    public ThirdParty getThirdPartyProfile(@PathVariable String username) {
        return authenticatorService.getThirdPartyProfile(username);
    }

    @PutMapping("/thirdparties/{username}")
    public String changeThirdPartyProfile(@PathVariable String username, @DTO(ThirdPartyDTO.class) ThirdParty thirdParty) {
        if (authenticatorService.changeThirdPartyProfile(thirdParty))
            return "Success!";
        else return "Oops, user "+thirdParty.getUsername()+" does not exists";
    }

}
