package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualEntity;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdPartyEntity;
import com.github.ferrantemattarutigliano.software.server.model.entity.UserEntity;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AuthenticatorService {

    private IndividualRepository individuals;
    private ThirdPartyRepository thirdparties;

    @Autowired
    public AuthenticatorService(@Qualifier("Individual")IndividualRepository individuals,
                                @Qualifier("ThirdParty") ThirdPartyRepository thirdparties) {
        this.individuals = individuals;
        this.thirdparties = thirdparties;
    }

    public Collection<IndividualEntity> getAllIndividuals() {
        return individuals.findAll().stream().collect(Collectors.toList());
    }

    public Collection<ThirdPartyEntity> getAllThirdParties() {
        return thirdparties.findAll().stream().collect(Collectors.toList());
    }

    public boolean individualRegistration(@DTO(IndividualDTO.class) IndividualEntity individual) {
        if (!userAlreadyExists(individual.getUsername())
                && validateIndividual(individual)) {
            individuals.save(individual);
            return true;
        }
        else return false;
    }

    public boolean thirdPartyRegistration(@DTO(ThirdPartyDTO.class) ThirdPartyEntity thirdParty) {
        if (!userAlreadyExists(thirdParty.getUsername())
                && validateThirdParty(thirdParty)) {
            thirdparties.save(thirdParty);
            return true;
        }
        else return false;
    }

    public boolean login(UserEntity user) {
        IndividualEntity registeredIndividuals = individuals.findByUsername(user.getUsername());
        ThirdPartyEntity registeredThirdParties = thirdparties.findByUsername(user.getUsername());
        if (registeredIndividuals != null)
            return user.getPassword().equals(registeredIndividuals.getPassword());
        else if (registeredThirdParties != null)
            return user.getPassword().equals(registeredThirdParties.getPassword());
        else return false;
    }

    public IndividualEntity getIndividualProfile(String username) {
        return individuals.findByUsername(username);
    }

    public boolean changeIndividualProfile(@DTO(IndividualDTO.class) IndividualEntity individual) {
        if (userAlreadyExists(individual.getUsername())
                && validateIndividual(individual)) {
            individuals.save(individual); /* FIX ME: id is different PK violation */
            return true;
        }
        else return false;
    }

    public ThirdPartyEntity getThirdPartyProfile(String username) {
        return thirdparties.findByUsername(username);
    }

    public boolean changeThirdPartyProfile(@DTO(ThirdPartyDTO.class) ThirdPartyEntity thirdParty) {
        if (userAlreadyExists(thirdParty.getUsername())
                && validateThirdParty(thirdParty)) {
            thirdparties.save(thirdParty); /* FIX ME id is different PK violation */
            return true;
        }
        else return false;
    }

    private boolean userAlreadyExists(String username) {
        return individuals.findUsername(username)
                || thirdparties.findUsername(username);
    }

    private boolean validateIndividual(IndividualEntity individual){
        return ssnIsValid(individual.getSsn())
                && emailIsValid(individual.getEmail());
    }

    private boolean validateThirdParty(ThirdPartyEntity thirdParty){
        return vatIsValid(thirdParty.getVat())
                && emailIsValid(thirdParty.getEmail());
    }

    private boolean emailIsValid(String email){
        return email != null
                && !email.equals("");
    }


    private boolean ssnIsValid(String ssn){
        return ssn != null
                && !ssn.equals("");
    }

    private boolean vatIsValid(String vat){
        return vat != null
                && !vat.equals("");
    }
}
