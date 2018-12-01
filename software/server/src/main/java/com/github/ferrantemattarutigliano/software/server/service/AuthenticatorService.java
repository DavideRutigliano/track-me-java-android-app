package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticatorService {

    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    public Collection<Individual> getAllIndividuals() {
        return individualRepository.findAll();
    }

    public Collection<ThirdParty> getAllThirdParties() {
        return thirdPartyRepository.findAll();
    }

    public boolean individualRegistration(Individual individual) {
        if (!userAlreadyExists(individual.getUsername())
                && validateIndividual(individual)) {
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public boolean thirdPartyRegistration(ThirdParty thirdParty) {
        if (!userAlreadyExists(thirdParty.getUsername())
                && validateThirdParty(thirdParty)) {
            thirdPartyRepository.save(thirdParty);
            return true;
        }
        else return false;
    }

    public boolean login(User user) { //TODO ADD HASHING TO LOGIN PASSWORD
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            if (individualRepository.existsByUsername(username)) {
                Individual individual = individualRepository.findByUsername(username);
                return password.equals(individual.getPassword());
            }
            if (thirdPartyRepository.existsByUsername(username)) {
                ThirdParty thirdParty = thirdPartyRepository.findByUsername(username);
                return password.equals(thirdParty.getPassword());
            }
        }
        catch (NullPointerException e){
            e.fillInStackTrace();//should never get here
        }
        return false;
    }

    public Individual getIndividualProfile(String username) {
        return individualRepository.findByUsername(username);
    }

    public boolean changeIndividualProfile(Individual individual) {
        if (userAlreadyExists(individual.getUsername())
                && validateIndividual(individual)) {
            individualRepository.save(individual); /* TODO FIX ME: id is different PK violation */
            return true;
        }
        else return false;
    }

    public ThirdParty getThirdPartyProfile(String username) {
        return thirdPartyRepository.findByUsername(username);
    }

    public boolean changeThirdPartyProfile(ThirdParty thirdParty) {
        if (userAlreadyExists(thirdParty.getUsername())
                && validateThirdParty(thirdParty)) {
            thirdPartyRepository.save(thirdParty); /* TODO FIX ME id is different PK violation */
            return true;
        }
        else return false;
    }

    private boolean userAlreadyExists(String username) {
        return individualRepository.existsByUsername(username)
                || thirdPartyRepository.existsByUsername(username);
    }

    private boolean validateIndividual(Individual individual){
        return ssnIsValid(individual.getSsn())
                && emailIsValid(individual.getEmail());
    }

    private boolean validateThirdParty(ThirdParty thirdParty){
        return vatIsValid(thirdParty.getVat())
                && emailIsValid(thirdParty.getEmail());
    }


    private boolean match(String regex, String toCompare) {
        if (toCompare == null)
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCompare);
        return matcher.matches();
    }

    private boolean emailIsValid(String email){
        return match("([a-z0-9][-a-z0-9_\\+\\.]*[a-z0-9])@([a-z0-9][-a-z0-9\\.]*[a-z0-9]\\.(com|it|org|net)|([0-9]{1,3}\\.{3}[0-9]{1,3}))", email);
    }

    private boolean ssnIsValid(String ssn){
        return match("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]", ssn);
    }

    private boolean vatIsValid(String vat){
        return match("^[A-Za-z]{2,4}(?=.{2,12}$)[-_\\s0-9]*(?:[a-zA-Z][-_\\s0-9]*){0,2}$", vat);
    }
}



