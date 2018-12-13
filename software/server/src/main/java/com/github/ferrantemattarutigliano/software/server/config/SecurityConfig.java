package com.github.ferrantemattarutigliano.software.server.config;

import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import com.github.ferrantemattarutigliano.software.server.token.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.sql.DataSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String USERS_QUERY = "SELECT username, password, 1 FROM user WHERE username = ?";
    private String AUTH_QUERY = "SELECT userId, 'ROLE_INDIVIDUAL' FROM individual " +
            "UNION " +
            "SELECT userId, 'ROLE_THIRD_PARTY' FROM thirdParty " +
            "WHERE userId = " +
            "(SELECT id FROM user WHERE username = ?)";

    @Autowired
    private AuthenticatorService authService;
    @Autowired
    private DataSource dataSource;

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
                .passwordEncoder(authService.passwordEncoder())
                .and()

                .authenticationProvider(authService.authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(AUTH_QUERY);
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