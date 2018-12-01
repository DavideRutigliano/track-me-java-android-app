package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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


    //TODO add regex to check email ssn and vat
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
