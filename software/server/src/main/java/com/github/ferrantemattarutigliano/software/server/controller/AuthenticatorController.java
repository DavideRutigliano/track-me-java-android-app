package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualEntity;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdPartyEntity;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.UserEntity;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class AuthenticatorController {
    private AuthenticatorService authenticator;

    public AuthenticatorController(AuthenticatorService authenticator) {
        this.authenticator = authenticator;
    }

    @GetMapping("/individuals")
    public Collection<IndividualEntity> getAllIndividuals() {
        return authenticator.getAllIndividuals();
    }

    @PostMapping("/individuals/add")
    public String individualRegistration(@DTO(IndividualDTO.class) IndividualEntity individual) {
        if (authenticator.individualRegistration(individual))
            return "Success!";
        else return "Oops, something went wrong.";
    }

    @GetMapping("/thirdparties")
    public Collection<ThirdPartyEntity> getAllThirdParties() { return authenticator.getAllThirdParties(); }

    @PostMapping("/thirdparties/add")
    public String thirdPartyRegistration(@DTO(ThirdPartyDTO.class) ThirdPartyEntity thirdParty) {
        if (authenticator.thirdPartyRegistration(thirdParty))
            return "Success!";
        else return "Oops, something went wrong.";
    }

    @PostMapping("/login")
    public String login(@DTO(UserDTO.class) UserEntity user) {
        if (authenticator.login(user))
            return "Welcome "+user.getUsername()+"!";
        else return "Oops, something went wrong.";
    }

    @GetMapping("/individuals/{username}")
    public IndividualEntity getIndividualProfile(@PathVariable String username) {
        return authenticator.getIndividualProfile(username);
    }

    @PutMapping("/individuals/{username}")
    public String changeIndividualProfile(@PathVariable String username, @DTO(IndividualDTO.class) IndividualEntity individual) {
        if (authenticator.changeIndividualProfile(individual))
            return "Success!";
        else return "Oops, user "+individual.getUsername()+" does not exists";
    }

    @GetMapping("/thirdparties/{username}")
    public ThirdPartyEntity getThirdPartyProfile(@PathVariable String username) {
        return authenticator.getThirdPartyProfile(username);
    }

    @PutMapping("/thirdparties/{username}")
    public String changeThirdPartyProfile(@PathVariable String username, @DTO(ThirdPartyDTO.class) ThirdPartyEntity thirdParty) {
        if (authenticator.changeThirdPartyProfile(thirdParty))
            return "Success!";
        else return "Oops, user "+thirdParty.getUsername()+" does not exists";
    }

}
