package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticatorService implements UserDetailsService {

    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    public boolean individualRegistration(Individual individual) {

        String plaintextPass = individual.getPassword();

        if (!individualAlreadyExists(individual.getUsername(), individual.getEmail(), individual.getSsn())
                && validateIndividual(individual)) {
            individual.setPassword(passwordEncoder().encode(plaintextPass));
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public boolean thirdPartyRegistration(ThirdParty thirdParty) {

        String plaintextPass = thirdParty.getPassword();

        if (!thirdPartyAlreadyExists(thirdParty.getUsername(), thirdParty.getEmail(), thirdParty.getVat())
                && validateThirdParty(thirdParty)) {
            thirdParty.setPassword(passwordEncoder().encode(plaintextPass));
            thirdPartyRepository.save(thirdParty);
            return true;
        }
        else return false;
    }

    public User login(User user) {

        String username = user.getUsername();
        String password = user.getPassword();

        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication auth = authenticationProvider().authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        User u = (User) auth.getPrincipal();

        if (auth.isAuthenticated()) {
            user.addRole(auth.getAuthorities().stream().findFirst().get().toString());
        }

        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        if (username == null || username.isEmpty())
            throw new UsernameNotFoundException("Please fill out username field");

        final User user;

        if (individualRepository.existsByUsername(username)) {
            user = individualRepository.findByUsername(username);
            user.addRole("INDIVIDUAL");
        } else {
            user = thirdPartyRepository.findByUsername(username);
            user.addRole("THIRD_PARTY");
        }
        if (user == null)
            throw new UsernameNotFoundException("Username does not exists");

        user.getAuthorities();
        return user;
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
                && !thirdPartyRepository.existsByUsername(newUsername)) {

            if (individualRepository.existsByUsername(oldUsername)) {
                Individual individual = individualRepository.findByUsername(oldUsername);
                if (passwordEncoder().matches(password, individual.getPassword())) {
                    individual.setUsername(newUsername);
                    individualRepository.save(individual);
                    return true;
                }
            }
            if (thirdPartyRepository.existsByUsername(oldUsername)) {
                ThirdParty thirdParty = thirdPartyRepository.findByUsername(oldUsername);
                if (passwordEncoder().matches(password, thirdParty.getPassword())) {
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
            if (passwordEncoder().matches(oldPassword, individual.getPassword())) {
                individual.setPassword(passwordEncoder().encode(newPassword));
                individualRepository.save(individual);
                return true;
            }
        }
        if (thirdPartyRepository.existsByUsername(username)) {
            ThirdParty thirdParty = thirdPartyRepository.findByUsername(username);
            if (passwordEncoder().matches(oldPassword, thirdParty.getPassword())) {
                thirdParty.setPassword(passwordEncoder().encode(newPassword));
                thirdPartyRepository.save(thirdParty);
                return true;
            }
        }
        return false;
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

        return match("[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]", ssn);
    }

    private boolean vatIsValid(String vat) {

        return match("[0-9]{11}", vat);
    }
}



