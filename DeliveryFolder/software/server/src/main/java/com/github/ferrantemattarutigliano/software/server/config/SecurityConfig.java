package com.github.ferrantemattarutigliano.software.server.config;

import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import com.github.ferrantemattarutigliano.software.server.token.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.sql.DataSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticatorService authService;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(@Lazy AuthenticatorService authService, DataSource dataSource) {
        this.authService = authService;
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(authService);
        return authenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter authenticationTokenFilter = new TokenAuthenticationFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder())
                .and()

                .authenticationProvider(authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .anonymous().disable()
                .csrf().disable()
                .exceptionHandling().and()

                .requestCache()
                .requestCache(new NullRequestCache()).and()

                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()

                .addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/registration/**").permitAll()
                .antMatchers("/individuals/**").hasRole("INDIVIDUAL")
                .antMatchers("/thirdparties/**").hasRole("THIRD_PARTY")
                .anyRequest()
                .authenticated();
    }

}