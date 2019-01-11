package com.github.ferrantemattarutigliano.software.server.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ferrantemattarutigliano.software.server.model.dto.UserDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.service.AuthenticatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

public class TokenAuthenticationFilterTest {

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;
    @Mock
    private TokenUtils mockTokenUtils;
    @Mock
    private DaoAuthenticationProvider mockAuthenticationProvider;
    @Mock
    private AuthenticatorService mockAuthenticatorService;
    @Mock
    private Authentication mockAuthentication;
    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private RequestDispatcher mockRequestDispatcher;
    @Mock
    private KeyBasedPersistenceTokenService mockTokenService;
    @Mock
    private Token mockToken;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private FilterChain mockFilterChain;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTokenFilter_Login() throws ServletException, IOException {

        UserDTO dummyDto = new UserDTO();
        dummyDto.setUsername("user");
        dummyDto.setPassword("pass");

        byte[] rawData;

        ObjectMapper mapper = new ObjectMapper();

        rawData = mapper.writeValueAsBytes(dummyDto);

        InputStream inputStream = new ByteArrayInputStream(rawData);

        class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            private ServletInputStream is;

            private ResettableServletInputStream(InputStream is) {
                this.stream = is;
            }

            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return this.is.isFinished();
            }

            @Override
            public boolean isReady() {
                return this.is.isReady();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.is.setReadListener(readListener);
            }
        }

        ServletInputStream resettable = new ResettableServletInputStream(inputStream);

        Mockito.when(mockRequest.getInputStream()).thenReturn(resettable);

        Mockito.when(mockRequest.getMethod()).thenReturn("POST");

        Mockito.when(mockRequest.getPathInfo()).thenReturn(null);

        Mockito.when(mockRequest.getServletPath()).thenReturn("/users/login");

        Mockito.when(mockRequest.getRequestDispatcher("/users/login")).thenReturn(mockRequestDispatcher);

        SecurityContextHolder.setContext(mockSecurityContext);

        String[] login = {
                dummyDto.getUsername(),
                dummyDto.getPassword()
        };

        Mockito.when(mockTokenService.allocateToken(login[0] + ":" + login[1]))
                .thenReturn(mockToken);

        final Integer SERVER_INTEGER = 32;
        final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
        final Integer PRN_BYTES = 16;
        final KeyBasedPersistenceTokenService tokenService;

        tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());

        String key = tokenService.allocateToken(login[0] + ":" + login[1]).getKey();

        Mockito.when(mockToken.getKey()).thenReturn(key);

        Mockito.when(mockTokenUtils.getUsernameAndPassFromToken(mockRequest)).thenReturn(login);

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login[0], login[1]);

        Mockito.when(mockAuthenticationProvider.authenticate(authReq)).thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.isAuthenticated()).thenReturn(true);

        Mockito.when(mockAuthenticatorService.getAuthenticationProvider()).thenReturn(mockAuthenticationProvider);

        long time = System.currentTimeMillis();

        Mockito.when(mockTokenUtils.getTokenCreationTime(mockRequest)).thenReturn(time);

        Mockito.when(mockResponse.getHeader("X-Auth-Token")).thenReturn(key);

        tokenAuthenticationFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        Assert.assertEquals(key, mockResponse.getHeader("X-Auth-Token"));
    }

    @Test
    public void testTokenFilter_GenericRequest() throws ServletException, IOException {

        UserDTO dummyDto = new UserDTO();
        dummyDto.setId(2L);
        dummyDto.setUsername("user");
        dummyDto.setPassword("pass");
        dummyDto.setEmail("em@ail.com");

        Mockito.when(mockRequest.getPathInfo()).thenReturn(null);

        Mockito.when(mockRequest.getServletPath()).thenReturn("/users/test");

        Mockito.when(mockRequest.getRequestDispatcher("/users/test")).thenReturn(mockRequestDispatcher);

        SecurityContextHolder.setContext(mockSecurityContext);

        String[] login = {
                dummyDto.getUsername(),
                dummyDto.getPassword()
        };

        Mockito.when(mockTokenService.allocateToken(login[0] + ":" + login[1]))
                .thenReturn(mockToken);

        final Integer SERVER_INTEGER = 32;
        final String SERVER_SECRET = "#SèRv3r_$3kRet:=Tr4CkM3_s.SécR37";
        final Integer PRN_BYTES = 16;
        final KeyBasedPersistenceTokenService tokenService;

        tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(SERVER_INTEGER);
        tokenService.setServerSecret(SERVER_SECRET);
        tokenService.setPseudoRandomNumberBytes(PRN_BYTES);
        tokenService.setSecureRandom(new SecureRandom());

        String key = tokenService.allocateToken(login[0] + ":" + login[1]).getKey();

        Mockito.when(mockToken.getKey()).thenReturn(key);

        Mockito.when(mockTokenUtils.getUsernameAndPassFromToken(mockRequest)).thenReturn(login);

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login[0], login[1]);

        Mockito.when(mockAuthenticationProvider.authenticate(authReq)).thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.isAuthenticated()).thenReturn(true);

        Mockito.when(mockAuthenticatorService.getAuthenticationProvider()).thenReturn(mockAuthenticationProvider);

        long time = System.currentTimeMillis();

        Mockito.when(mockTokenUtils.getTokenCreationTime(mockRequest)).thenReturn(time);

        Mockito.when(mockResponse.getHeader("X-Auth-Token")).thenReturn(key);

        tokenAuthenticationFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        Assert.assertEquals(key, mockResponse.getHeader("X-Auth-Token"));
    }
}
