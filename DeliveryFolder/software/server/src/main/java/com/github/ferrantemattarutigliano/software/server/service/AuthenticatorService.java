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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticatorService implements UserDetailsService {

    private final UserRepository userRepository;
    private final IndividualRepository individualRepository;
    private final ThirdPartyRepository thirdPartyRepository;
    private final DaoAuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticatorService(UserRepository userRepository, IndividualRepository individualRepository, ThirdPartyRepository thirdPartyRepository, DaoAuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.individualRepository = individualRepository;
        this.thirdPartyRepository = thirdPartyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
    }

    public DaoAuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
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

        individual.getUser().setPassword(passwordEncoder.encode(plaintextPass));
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

        thirdParty.getUser().setPassword(passwordEncoder.encode(plaintextPass));
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


        Authentication authentication;

        try {
            authentication = authenticationProvider.authenticate(authReq);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return null;
        }

        securityContext.setAuthentication(authentication);

        return (User) authentication.getPrincipal();
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
        return individualRepository.findByUser(authenticated);
    }

    public String changeIndividualProfile(String username, Individual updatedIndividual) {
        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticated == null) {
            return Message.BAD_LOGIN.toString();
        }

        boolean isCorrectWeight = updatedIndividual.getWeight() > -1;
        boolean isCorrectHeight = updatedIndividual.getHeight() > -1;

        if (!(isCorrectWeight && isCorrectHeight)) {
            return Message.BAD_PARAMETERS.toString();
        }

        Individual individual = individualRepository.findByUser(authenticated);

        if (updatedIndividual.getFirstname() != null
                && !updatedIndividual.getFirstname().isEmpty()) {
            individual.setFirstname(updatedIndividual.getFirstname());
        }

        if (updatedIndividual.getLastname() != null
                && !updatedIndividual.getLastname().isEmpty()) {
            individual.setLastname(updatedIndividual.getLastname());
        }

        if (updatedIndividual.getState() != null
                && !updatedIndividual.getState().isEmpty()) {
            individual.setState(updatedIndividual.getState());
        }

        if (updatedIndividual.getCity() != null
                && !updatedIndividual.getCity().isEmpty()) {
            individual.setCity(updatedIndividual.getCity());
        }

        if (updatedIndividual.getAddress() != null
                && !updatedIndividual.getAddress().isEmpty()) {
            individual.setAddress(updatedIndividual.getAddress());
        }

        if (updatedIndividual.getHeight() != 0) {
            individual.setHeight(updatedIndividual.getHeight());
        }

        if (updatedIndividual.getWeight() != 0) {
            individual.setWeight(updatedIndividual.getWeight());
        }

        individualRepository.save(individual);

        return Message.CHANGE_PROFILE_SUCCESS.toString();
    }

    public ThirdParty getThirdPartyProfile(String username) {
        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return thirdPartyRepository.findByUser(authenticated);
    }

    public String changeThirdPartyProfile(String username, ThirdParty updatedThirdParty) {

        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticated == null || !thirdPartyRepository.existsByUser(authenticated)) {
            return Message.BAD_REQUEST.toString();
        }
        if (!validateThirdParty(updatedThirdParty)) {
            return Message.BAD_PARAMETERS.toString();
        }

        ThirdParty thirdParty = thirdPartyRepository.findByUser(authenticated);

        if (updatedThirdParty.getOrganizationName() != null
                && !updatedThirdParty.getOrganizationName().isEmpty()) {
            thirdParty.setOrganizationName(updatedThirdParty.getOrganizationName());
        }

        thirdPartyRepository.save(thirdParty);

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

        user.setPassword(passwordEncoder.encode(newPassword));
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