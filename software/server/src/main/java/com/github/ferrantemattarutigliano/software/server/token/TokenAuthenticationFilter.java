package com.github.ferrantemattarutigliano.software.server.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ferrantemattarutigliano.software.server.config.ContentCacheRequestWrapper;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ContentCacheRequestWrapper requestWrapper = null;
        String currentLink = (httpRequest.getPathInfo() == null)
                ? (httpRequest.getServletPath())
                : (httpRequest.getPathInfo() + httpRequest.getServletPath());

        if (!currentLink.equals("/error")                               /*  Do not check  */
                && !currentLink.equals("/registration/individual")      /*  X-Auth-Token  */
                && !currentLink.equals("/registration/thirdparty")) {   /* for these url  */

            if (currentLink.equals("/login")) {

                requestWrapper = new ContentCacheRequestWrapper(httpRequest); /* cache login request */
                doLogin(requestWrapper, httpResponse);                        /* content in order to */
                requestWrapper.resetInputStream();                            /* get the body twice. */
            } else if (currentLink.equals("/logout")) {

                doLogout(httpRequest);
            } else checkLogin(httpRequest, httpResponse);
        }

        /* Forward request to controllers and end the filters chain */
        final RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher(currentLink);

        try {
            requestDispatcher.forward((requestWrapper == null) ? httpRequest : requestWrapper, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void doLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {

            String requestBody = extractPostRequestBody(httpRequest);

            ObjectMapper objectMapper = new ObjectMapper();
            User user = (User) objectMapper.readValue(requestBody, User.class);

            String username = user.getUsername();
            String password = user.getPassword();

            if (username != null && !username.isEmpty())
                tokenUtils.addHeader(httpResponse, username, password);
        }

    }

    private static String extractPostRequestBody(HttpServletRequest request) {

        Scanner s = null;
        try {
            s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.hasNext() ? s.next() : "";
    }

    private void checkLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        String tokenInfo = tokenUtils.getUsernameAndPassFromToken(httpRequest);
        String[] login = tokenInfo.split(":");
        String username = login[0];
        String password = login[1];

        if (username != null && !username.isEmpty()) {

            checkUsernameAndPassword(username, password, httpResponse);
        }
    }

    private void checkUsernameAndPassword(String username, String password, HttpServletResponse httpResponse) throws IOException {

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authService.authenticationProvider().authenticate(authReq);

        if (authentication.isAuthenticated()) {
            tokenUtils.addHeader(httpResponse, username, password);
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void doLogout(HttpServletRequest httpRequest) {
        tokenUtils.deleteToken(httpRequest);
    }
}
