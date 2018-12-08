package com.github.ferrantemattarutigliano.software.server.config;

import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.savedrequest.NullRequestCache;


import javax.sql.DataSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticatorService authService;
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(authService)
                .passwordEncoder(authService.passwordEncoder())
                .and()

                .authenticationProvider(authService.authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, 1 FROM user WHERE username = ?");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .formLogin()
                .disable()

                .logout()
                .disable()

                .exceptionHandling()
                .and()

                .requestCache()
                .requestCache(new NullRequestCache())
                .and()

                .csrf().disable()

                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()

                .authorizeRequests()
                //.antMatchers("/login").permitAll()
                //.antMatchers("/registration/**").permitAll()
                //.antMatchers("/individuals/**").hasRole("INDIVIDUAL")
                //.antMatchers("thirdparties/**").hasRole("THIRD_PARTY")
                .anyRequest()
                .permitAll(); //NO SECURITY
        //.authenticated();
    }

}