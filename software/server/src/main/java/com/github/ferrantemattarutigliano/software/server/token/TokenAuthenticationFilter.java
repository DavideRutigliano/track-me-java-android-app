package com.github.ferrantemattarutigliano.software.server.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticatorService authService;

    private Long TOKEN_VALIDITY_PERIOD = 1800000L; //token valid half an hour

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ContentCacheRequestWrapper requestWrapper = null;

        String currentLink = (httpRequest.getPathInfo() == null)
                ? (httpRequest.getServletPath())
                : (httpRequest.getPathInfo() + httpRequest.getServletPath());


        if (currentLink.equals("/users/login")) {

            requestWrapper = new ContentCacheRequestWrapper(httpRequest);
            doLogin(requestWrapper, httpResponse);
            requestWrapper.resetInputStream();
        } else if (!currentLink.equals("/users/registration/individual")
                && !currentLink.equals("/users/registration/thirdparty")) {

            checkLogin(httpRequest, httpResponse);

            if (currentLink.equals("/users/logout"))
                doLogout(httpResponse);
        }

        if (!currentLink.equals("/error")
                && httpResponse.getStatus() != HttpServletResponse.SC_FORBIDDEN
                && httpResponse.getStatus() != HttpServletResponse.SC_UNAUTHORIZED) {

            final RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher(currentLink);
            requestDispatcher.forward((requestWrapper == null) ? httpRequest : requestWrapper, httpResponse);
        }
    }

    private void doLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {

            String requestBody = extractPostRequestBody(httpRequest);

            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(requestBody, User.class);

            String username = user.getUsername();
            String password = user.getPassword();

            if (username != null && !username.isEmpty())
                tokenUtils.addHeader(httpResponse, username, password);

        }
    }

    private static String extractPostRequestBody(HttpServletRequest request) {

        Scanner s;
        try {
            s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        } catch (IOException e) {
            return null;
        }
        return s.hasNext() ? s.next() : "";
    }

    private void checkLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        String[] login = tokenUtils.getUsernameAndPassFromToken(httpRequest);

        if (login == null) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            Long creationTime;

            try {
                creationTime = tokenUtils.getTokenCreationTime(httpRequest);
            } catch (Exception e) {
                creationTime = 0L;
            }

            String username = login[0];
            String password = login[1];
            Long currentTime = System.currentTimeMillis();

            if (currentTime - creationTime <= TOKEN_VALIDITY_PERIOD)
                checkUsernameAndPassword(username, password, httpResponse);
            else {
                tokenUtils.deleteToken(httpResponse);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }

    private void checkUsernameAndPassword(String username, String password, HttpServletResponse httpResponse) throws IOException {

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = null;

        try {
            authentication = authService.getAuthenticationProvider().authenticate(authReq);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        if (authentication != null
                && authentication.isAuthenticated()) {
            tokenUtils.addHeader(httpResponse, username, password);
            securityContext.setAuthentication(authentication);
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void doLogout(HttpServletResponse httpResponse) {
        tokenUtils.deleteToken(httpResponse);
    }
}
