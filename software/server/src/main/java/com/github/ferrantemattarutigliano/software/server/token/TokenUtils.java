package com.github.ferrantemattarutigliano.software.server.token;

import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;

@Component
public class TokenUtils {

    private final String HEADER_SECURITY_TOKEN = "X-Auth-Token";
    private final Integer SERVER_INTEGER = 32;
    private final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
    private final Integer PRN_BYTES = 16;

    private KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();

    @PostConstruct
    private void init() {
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());
    }

    public String getUsernameAndPassFromToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_SECURITY_TOKEN);
        return (header == null || header.isEmpty()) ? "" : extractUserNameAndPassword(header);
    }

    private String extractUserNameAndPassword(String value) {
        Token token = tokenService.verifyToken(value);
        String[] login = token.getExtendedInformation().split(":");
        return login[0] + ":" + login[1];
    }

    public void addHeader(HttpServletResponse response, String username, String password) {
        String encryptedValue = createAuthToken(username, password);
        response.setHeader(HEADER_SECURITY_TOKEN, encryptedValue);
    }

    private String createAuthToken(String username, String password) {
        Token token = tokenService.allocateToken(username + ":" + password);
        return token.getKey();
    }

    public void deleteToken(HttpServletRequest httpRequest) {
        httpRequest.setAttribute(HEADER_SECURITY_TOKEN, "");
    }

}