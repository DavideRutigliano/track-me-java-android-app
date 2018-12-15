package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
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
    private UserRepository userRepository;
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

    public String individualRegistration(Individual individual) {

        String plaintextPass = individual.getUser().getPassword();

        if (individualAlreadyExists(individual.getUser().getUsername(),
                individual.getUser().getEmail(),
                individual.getSsn())) {
            return Message.INDIVIDUAL_ALREADY_EXISTS.toString();
        }

        if (!emailIsValid(individual.getUser().getEmail())) {
            return Message.INVALID_EMAIL.toString();
        }

        if (!validateIndividual(individual)) {
            return Message.BAD_PARAMETERS.toString();
        }

        individual.getUser().setPassword(passwordEncoder().encode(plaintextPass));
        userRepository.save(individual.getUser());
        individualRepository.save(individual);

        return Message.REGISTRATION_SUCCESS.toString();
    }

    public String thirdPartyRegistration(ThirdParty thirdParty) {

        String plaintextPass = thirdParty.getUser().getPassword();
        User user = thirdParty.getUser();

        if (thirdPartyAlreadyExists(user.getUsername(),
                user.getEmail(),
                thirdParty.getVat())) {
            return Message.THIRD_PARTY_ALREADY_EXISTS.toString();
        }

        if (!emailIsValid(user.getEmail())) {
            return Message.INVALID_EMAIL.toString();
        }

        if (!validateThirdParty(thirdParty)) {
            return Message.BAD_PARAMETERS.toString();
        }

        thirdParty.getUser().setPassword(passwordEncoder().encode(plaintextPass));
        userRepository.save(thirdParty.getUser());
        thirdPartyRepository.save(thirdParty);

        return Message.REGISTRATION_SUCCESS.toString();
    }

    public User login(User user) {

        String username = user.getUsername();
        String password = user.getPassword();
        SecurityContext securityContext = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(username, password);


        Authentication auth;

        try {
            auth = authenticationProvider().authenticate(authReq);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return null;
        }

        securityContext.setAuthentication(auth);

        return (User) auth.getPrincipal();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        if (username == null || username.isEmpty())
            throw new UsernameNotFoundException(Message.USERNAME_IS_EMPTY.toString());

        final User user;

        if (userRepository.existsByUsername(username)) {

            user = userRepository.findByUsername(username);

            if (individualRepository.existsByUser(user))
                user.addRole(Role.ROLE_INDIVIDUAL.toString());

            else if (thirdPartyRepository.existsByUser(user))
                user.addRole(Role.ROLE_THIRD_PARTY.toString());
        } else
            throw new UsernameNotFoundException(Message.USERNAME_DO_NOT_EXISTS.toString());

        user.getAuthorities();

        return user;
    }

    public Individual getIndividualProfile(String username) {

        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticated != null
                && authenticated.getUsername().equals(username)
                && individualRepository.existsByUser(authenticated))
            return individualRepository.findByUser(authenticated);
        else return null;
    }

    public String changeIndividualProfile(String username, Individual individual) {

        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticated == null) {
            return Message.BAD_LOGIN.toString();
        }

        if (!authenticated.getUsername().equals(username)
                || !individualRepository.existsByUser(authenticated)) {
            return Message.BAD_REQUEST.toString();
        }

        if (!validateIndividual(individual)) {
            return Message.BAD_PARAMETERS.toString();
        }

        if (!individualRepository.existsBySsn(individual.getSsn())
                && !individualRepository.findByUser(authenticated).getSsn().equals(individual.getSsn())) {
            return Message.BAD_SSN_UPDATE.toString() + individualRepository.findByUser(authenticated).getSsn();
        }

        Individual i = individualRepository.findBySsn(individual.getSsn());

        if (individual.getFirstname() != null
                && !i.getFirstname().equals(individual.getFirstname())) {

            i.setFirstname(individual.getFirstname());
        }

        if (individual.getLastname() != null
                && !i.getLastname().equals(individual.getLastname())) {

            i.setLastname(individual.getLastname());
        }

        if (individual.getBirthdate() != null
                && !i.getBirthdate().equals(individual.getBirthdate())) {

            i.setBirthdate(individual.getBirthdate());
        }

        individualRepository.save(i);

        return Message.CHANGE_PROFILE_SUCCESS.toString();
    }

    public ThirdParty getThirdPartyProfile(String username) {

        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticated != null
                && authenticated.getUsername().equals(username)
                && thirdPartyRepository.existsByUser(authenticated))
            return thirdPartyRepository.findByUser(authenticated);
        else return null;
    }

    public String changeThirdPartyProfile(String username, ThirdParty thirdParty) {

        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticated == null) {
            return Message.BAD_LOGIN.toString();
        }

        if (!authenticated.getUsername().equals(username)
                || !thirdPartyRepository.existsByUser(authenticated)) {
            return Message.BAD_REQUEST.toString();
        }

        if (validateThirdParty(thirdParty)) {
            return Message.BAD_PARAMETERS.toString();
        }

        if (thirdPartyRepository.existsByVat(thirdParty.getVat())
                && !thirdPartyRepository.findByUser(authenticated).getVat().equals(thirdParty.getVat())) {
            return Message.BAD_VAT_UPDATE.toString() + thirdPartyRepository.findByUser(authenticated).getVat();
        }

        ThirdParty tp = thirdPartyRepository.findByVat(thirdParty.getVat());

        if (tp != null
                && thirdParty.getOrganizationName() != null
                && !tp.getOrganizationName().equals(thirdParty.getOrganizationName())) {

            tp.setOrganizationName(thirdParty.getOrganizationName());
        }

        thirdPartyRepository.save(tp);

        return Message.CHANGE_PROFILE_SUCCESS.toString();
    }

    public String changeUsername(String oldUsername, String newUsername) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            return Message.BAD_LOGIN.toString();
        }

        if (!user.getUsername().equals(oldUsername)
                || !userRepository.existsByUsername(oldUsername)) {
            return Message.BAD_REQUEST.toString();
        }

        if (userRepository.existsByUsername(newUsername)) {
            return Message.USERNAME_ALREADY_EXISTS.toString() + newUsername;
        }

        if (individualRepository.existsByUser(user)) {
            Individual individual = individualRepository.findByUser(user);
            user.setUsername(newUsername);
            userRepository.save(user);
            individual.setUser(user);
            individualRepository.save(individual);
        } else if (thirdPartyRepository.existsByUser(user)) {
            ThirdParty thirdParty = thirdPartyRepository.findByUser(user);
            user.setUsername(newUsername);
            userRepository.save(user);
            thirdParty.setUser(user);
            thirdPartyRepository.save(thirdParty);
        }
        return Message.CHANGE_USERNAME_SUCCESS.toString() + newUsername;
    }

    public String changePassword(String username, String newPassword) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            return Message.BAD_LOGIN.toString();
        }

        if (!user.getUsername().equals(username)
                || !userRepository.existsByUsername(username)) {
            return Message.BAD_REQUEST.toString();
        }

        user.setPassword(passwordEncoder().encode(newPassword));
        userRepository.save(user);

        return Message.CHANGE_PASSWORD_SUCCESS.toString();
    }

    private boolean individualAlreadyExists(String username, String email, String ssn) {

        return (userRepository.existsByUsername(username)
                || userRepository.existsByEmail(email)
                || individualRepository.existsBySsn(ssn));
    }

    private boolean thirdPartyAlreadyExists(String username, String email, String vat) {

        return (userRepository.existsByUsername(username)
                || userRepository.existsByEmail(email)
                || thirdPartyRepository.existsByVat(vat));
    }

    private boolean validateIndividual(Individual individual) {

        return ssnIsValid(individual.getSsn());
    }

    private boolean validateThirdParty(ThirdParty thirdParty) {

        return vatIsValid(thirdParty.getVat());
    }

    private boolean match(String regex, String toCompare) {

        if (toCompare == null)
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCompare);
        return matcher.matches();
    }

    private boolean emailIsValid(String email) {
        return match("^(.+)@(.+)$", email);
    }

    private boolean ssnIsValid(String ssn) {
        return match("[0-9]{9}", ssn);
    }

    private boolean vatIsValid(String vat) {

        return match("[0-9]{11}", vat);
    }
}