package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticatorService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

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
        return match("[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]", ssn);
    }

    private boolean vatIsValid(String vat) {
        return match("[0-9]{11}", vat);
    }

    public boolean individualRegistration(Individual individual) {
        String plaintextPass = individual.getPassword();
        if (!individualAlreadyExists(individual.getUsername(), individual.getEmail(), individual.getSsn())
                && validateIndividual(individual)) {
            individual.getAuthorities();
            individual.setPassword(passwordEncoder.encode(plaintextPass));
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public boolean thirdPartyRegistration(ThirdParty thirdParty) {
        String plaintextPass = thirdParty.getPassword();
        if (!thirdPartyAlreadyExists(thirdParty.getUsername(), thirdParty.getEmail(), thirdParty.getVat())
                && validateThirdParty(thirdParty)) {
            thirdParty.getAuthorities();
            thirdParty.setPassword(passwordEncoder.encode(plaintextPass));
            thirdPartyRepository.save(thirdParty);
            return true;
        }
        else return false;
    }

    public boolean login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            if (individualRepository.existsByUsername(username)) {
                Individual individual = individualRepository.findByUsername(username);
                return passwordEncoder.matches(password, individual.getPassword());
            }
            if (thirdPartyRepository.existsByUsername(username)) {
                ThirdParty thirdParty = thirdPartyRepository.findByUsername(username);
                return passwordEncoder.matches(password, thirdParty.getPassword());
            }
        }
        catch (NullPointerException e){
            e.fillInStackTrace(); /* should never get here */
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

    public boolean changeUsername(User user, String newUsername) {
        String oldUsername = user.getUsername();
        String password = user.getPassword();

        if (!individualRepository.existsByUsername(newUsername)
                && thirdPartyRepository.existsByUsername(newUsername)) {

            if (individualRepository.existsByUsername(oldUsername)) {
                Individual individual = individualRepository.findByUsername(oldUsername);
                if (passwordEncoder.matches(password, individual.getPassword())) {
                    individual.setUsername(newUsername);
                    individualRepository.save(individual);
                    return true;
                }
            }
            if (thirdPartyRepository.existsByUsername(oldUsername)) {
                ThirdParty thirdParty = thirdPartyRepository.findByUsername(oldUsername);
                if (passwordEncoder.matches(password, thirdParty.getPassword())) {
                    thirdParty.setUsername(newUsername);
                    thirdPartyRepository.save(thirdParty);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean changePassword(User user, String newPassword) {
        String username = user.getUsername();
        String oldPassword = user.getPassword();

        if (individualRepository.existsByUsername(username)) {
            Individual individual = individualRepository.findByUsername(username);
            if (passwordEncoder.matches(oldPassword, individual.getPassword())) {
                individual.setPassword(passwordEncoder.encode(newPassword));
                individualRepository.save(individual);
                return true;
            }
        }
        if (thirdPartyRepository.existsByUsername(username)) {
            ThirdParty thirdParty = thirdPartyRepository.findByUsername(username);
            if (passwordEncoder.matches(oldPassword, thirdParty.getPassword())) {
                thirdParty.setPassword(passwordEncoder.encode(newPassword));
                thirdPartyRepository.save(thirdParty);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty())
            throw new UsernameNotFoundException("Please fill out username field");

        final User user;
        if (individualRepository.existsByUsername(username))
            user = individualRepository.findByUsername(username);
        else
            user = thirdPartyRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Username does not exists");

        return user;
    }
}



