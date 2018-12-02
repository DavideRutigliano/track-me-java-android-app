package com.github.ferrantemattarutigliano.software.server.service;

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

    private boolean individualAlreadyExists(String username, String email, String ssn) {
        return (individualRepository.existsByUsername(username)
                || individualRepository.existsByEmail(email)
                || individualRepository.existsBySsn(ssn));
    }

    private boolean thirdPartyAlreadyExists(String username, String email, String vat) {
        return (thirdPartyRepository.existsByUsername(username)
                || thirdPartyRepository.existsByEmail(email)
                || thirdPartyRepository.existsByVat(vat));
    }

    private boolean validateIndividual(Individual individual) {
        return ssnIsValid(individual.getSsn())
                && emailIsValid(individual.getEmail().toLowerCase());
    }

    private boolean validateThirdParty(ThirdParty thirdParty) {
        return vatIsValid(thirdParty.getVat())
                && emailIsValid(thirdParty.getEmail().toLowerCase());
    }


    private boolean match(String regex, String toCompare) {
        if (toCompare == null)
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCompare);
        return matcher.matches();
    }

    private boolean emailIsValid(String email) {
        return match("([a-z0-9][-a-z0-9_\\+\\.]*[a-z0-9])@([a-z0-9][-a-z0-9\\.]*[a-z0-9]\\.(com|it|org|net|co\\.uk|edu)|([0-9]{1,3}\\.{3}[0-9]{1,3}))", email);
    }

    private boolean ssnIsValid(String ssn) {
        return true; //match("[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]", ssn);
    }

    private boolean vatIsValid(String vat) {
        return match("[0-9]{11}", vat);
    }

    public boolean individualRegistration(Individual individual) {
        if (!individualAlreadyExists(individual.getUsername(), individual.getEmail(), individual.getSsn())
                && validateIndividual(individual)) {
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public boolean thirdPartyRegistration(ThirdParty thirdParty) {
        if (!thirdPartyAlreadyExists(thirdParty.getUsername(), thirdParty.getEmail(), thirdParty.getVat())
                && validateThirdParty(thirdParty)) {
            thirdPartyRepository.save(thirdParty);
            return true;
        }
        else return false;
    }

    public boolean login(User user) { //TODO "real" login + session management
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
        if (individualAlreadyExists(individual.getUsername(), individual.getEmail(), individual.getSsn())
                && validateIndividual(individual)) {
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public ThirdParty getThirdPartyProfile(String username) {
        return thirdPartyRepository.findByUsername(username);
    }

    public boolean changeThirdPartyProfile(ThirdParty thirdParty) {
        if (thirdPartyAlreadyExists(thirdParty.getUsername(), thirdParty.getEmail(), thirdParty.getVat())
                && validateThirdParty(thirdParty)) {
            thirdPartyRepository.save(thirdParty);
            return true;
        }
        else return false;
    }
}



