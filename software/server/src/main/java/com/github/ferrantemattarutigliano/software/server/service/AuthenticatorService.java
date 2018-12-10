package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
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

    public boolean individualRegistration(Individual individual) {

        String plaintextPass = individual.getUser().getPassword();

        if (!individualAlreadyExists(individual.getUser().getUsername(), individual.getUser().getEmail(), individual.getSsn())
                && emailIsValid(individual.getUser().getEmail())
                && validateIndividual(individual)) {
            individual.getUser().setPassword(passwordEncoder().encode(plaintextPass));
            userRepository.save(individual.getUser());
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public boolean thirdPartyRegistration(ThirdParty thirdParty) {

        String plaintextPass = thirdParty.getUser().getPassword();

        if (!thirdPartyAlreadyExists(thirdParty.getUser().getUsername(), thirdParty.getUser().getEmail(), thirdParty.getVat())
                && emailIsValid(thirdParty.getUser().getEmail())
                && validateThirdParty(thirdParty)) {
            thirdParty.getUser().setPassword(passwordEncoder().encode(plaintextPass));
            userRepository.save(thirdParty.getUser());
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

        return (User) auth.getPrincipal();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        if (username == null || username.isEmpty())
            throw new UsernameNotFoundException("Username is empty");

        final User user;

        if (userRepository.existsByUsername(username)) {

            user = userRepository.findByUsername(username);

            if (individualRepository.existsByUser(user))
                user.addRole("INDIVIDUAL");

            else if (thirdPartyRepository.existsByUser(user))
                user.addRole("THIRD_PARTY");

        } else
            throw new UsernameNotFoundException("Username does not exists");

        user.getAuthorities();
        return user;
    }

    public Individual getIndividualProfile(String username) {
        User user = userRepository.findByUsername(username);
        return individualRepository.findByUser(user);
    }

    public boolean changeIndividualProfile(Individual individual) {

        if (validateIndividual(individual)) {
            individualRepository.save(individual);
            return true;
        }
        else return false;
    }

    public ThirdParty getThirdPartyProfile(String username) {
        User user = userRepository.findByUsername(username);
        return thirdPartyRepository.findByUser(user);
    }

    public boolean changeThirdPartyProfile(ThirdParty thirdParty) {

        if (validateThirdParty(thirdParty)) {
            thirdPartyRepository.save(thirdParty);
            return true;
        }
        else return false;
    }

    public boolean changeUsername(User user, String newUsername) {

        String oldUsername = user.getUsername();
        String password = user.getPassword();

        if (userRepository.existsByUsername(oldUsername)
                && !userRepository.existsByUsername(newUsername)) {

            User u = userRepository.findByUsername(oldUsername);
            if (passwordEncoder().matches(password, u.getPassword())) {

                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean changePassword(User user, String newPassword) {

        String username = user.getUsername();
        String oldPassword = user.getPassword();

        if (userRepository.existsByUsername(username)) {
            User u = userRepository.findByUsername(username);
            if (passwordEncoder().matches(oldPassword, u.getPassword())) {
                u.setPassword(newPassword);
                userRepository.save(u);
                return true;
            }
        }
        return false;
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

        return match("([a-z0-9][-a-z0-9_\\+\\.]*[a-z0-9])@([a-z0-9][-a-z0-9\\.]*[a-z0-9]\\.(com|it|org|net|co\\.uk|edu)|([0-9]{1,3}\\.{3}[0-9]{1,3}))", email);
    }

    private boolean ssnIsValid(String ssn) {

        return match("[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]", ssn);
    }

    private boolean vatIsValid(String vat) {

        return match("[0-9]{11}", vat);
    }
}



