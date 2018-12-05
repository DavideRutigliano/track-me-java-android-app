package com.github.ferrantemattarutigliano.software.server.config;

import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticatorService authService;
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService)
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, 1 FROM individual "
                                        + "UNION "
                                        + "SELECT username, password, 1 FROM third_party "
                                        + "WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, 'ROLE_INDIVIDUAL' FROM individual "
                                            + "UNION "
                                            + "SELECT username, 'ROLE_THIRD_PARTY' FROM third_party "
                                            + "WHERE username = ?");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/registration/**").permitAll()
                .antMatchers("/individual/**").hasRole("INDIVIDUAL")
                .antMatchers("/thirdparies/**").hasRole("THIRD_PARTY")
                .anyRequest().authenticated()
                .and().authorizeRequests()
                .antMatchers("/login").permitAll()
                .and().formLogin()
                .loginPage("/login").permitAll()
                .failureForwardUrl("/login")
                .and().logout()
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .and().csrf().disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}