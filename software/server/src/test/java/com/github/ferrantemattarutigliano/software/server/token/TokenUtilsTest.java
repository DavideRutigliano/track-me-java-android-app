package com.github.ferrantemattarutigliano.software.server.token;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;

import static org.mockito.ArgumentMatchers.any;

public class TokenUtilsTest {

    @InjectMocks
    private TokenUtils tokenUtils;

    @Mock
    private MockHttpServletRequest mockRequest1;

    @Mock
    private MockHttpServletResponse mockResponse;

    @Mock
    private Token mockToken;


    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUsernameAndPassFromTokenTest() {

        final String HEADER_SECURITY_TOKEN = "X-Auth-Token";
        final Integer SERVER_INTEGER = 32;
        final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
        final Integer PRN_BYTES = 16;
        final KeyBasedPersistenceTokenService tokenService;

        tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());

        String key = tokenService.allocateToken("username" + ":" + "password").getKey();



        String[] ExRes = new String[2];
        ExRes[0] = "username";
        ExRes[1] = "password";


        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-Auth-Token", key);


        String[] result = tokenUtils.getUsernameAndPassFromToken(mockHttpServletRequest);

        Assert.assertEquals(ExRes[0], result[0]);
        Assert.assertEquals(ExRes[1], result[1]);
    }

    @Test
    public void getUsernameAndPassFromTokenTest_exception() {

        final String HEADER_SECURITY_TOKEN = "X-Auth-Token";
        final Integer SERVER_INTEGER = 32;
        final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
        final Integer PRN_BYTES = 16;
        final KeyBasedPersistenceTokenService tokenService;

        tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());

        String key = tokenService.allocateToken("username" + ":" + "password").getKey();


        String[] ExRes = new String[2];
        ExRes[0] = "username";
        ExRes[1] = "password";


        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-Auth-Token", key +
                "sbaglia");


        String[] result = tokenUtils.getUsernameAndPassFromToken(mockHttpServletRequest);

    }

    @Test
    public void getCreationTimeTest() {

        final String HEADER_SECURITY_TOKEN = "X-Auth-Token";
        final Integer SERVER_INTEGER = 32;
        final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
        final Integer PRN_BYTES = 16;
        final KeyBasedPersistenceTokenService tokenService;

        tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());

        String key = tokenService.allocateToken("username" + ":" + "password").getKey();

        mockToken = tokenService.verifyToken(key);

        Mockito.when(mockRequest1.getHeader("X-Auth-Token"))
                .thenReturn(key);


        String[] ExRes = new String[2];
        ExRes[0] = "username";
        ExRes[1] = "password";


        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-Auth-Token", key);


        long result = tokenUtils.getTokenCreationTime(mockHttpServletRequest);

        Assert.assertEquals(mockToken.getKeyCreationTime(), result);
    }

    @Test
    public void getCreationTimeTest_exeption() {

        final String HEADER_SECURITY_TOKEN = "X-Auth-Token";
        final Integer SERVER_INTEGER = 32;
        final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
        final Integer PRN_BYTES = 16;
        final KeyBasedPersistenceTokenService tokenService;

        tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());

        String key = tokenService.allocateToken("username" + ":" + "password").getKey();

        mockToken = tokenService.verifyToken(key);
        

        String[] ExRes = new String[2];
        ExRes[0] = "username";
        ExRes[1] = "password";


        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-Auth-Token", key + "wrong");


        long result = tokenUtils.getTokenCreationTime(mockHttpServletRequest);

        Assert.assertEquals(0L, result);
    }

    @Test
    public void addHeaderTest() {

        tokenUtils.addHeader(mockResponse, "username", "password");

    }

    @Test
    public void deleteTokenTest() {

        tokenUtils.deleteToken(mockResponse);

    }
/*
    @Test
    public void createAuthenticationTokenTest() {

        String Result =tokenUtils.createAuthToken("username","password");

    }
*/
}

