package com.github.ferrantemattarutigliano.software.client.httprequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class HttpTaskTest {
    @Mock
    private AsyncResponse<DummyReturnClass> asyncResponse;
    @Mock
    private HttpInformationContainer httpInformationContainer;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private HttpTask<DummyReturnClass> httpTask
            = new DummyTask(DummyReturnClass.class, asyncResponse);

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doInBackgroundTest(){
        httpTask.setHttpInformationContainer(httpInformationContainer);
        //test post method
        HttpMethod httpMethod = HttpMethod.POST;
        Object parameter = new DummyParameter("test");
        Mockito.when(httpInformationContainer.getHttpMethod())
                .thenReturn(httpMethod);
        Mockito.when(httpInformationContainer.getParameter())
                .thenReturn(parameter);
        //mock server response
        ResponseEntity<DummyReturnClass> serverResponse
                = new ResponseEntity<>(new DummyReturnClass(), HttpStatus.ACCEPTED);
        //mock send request to server and get a response
        Mockito.when(restTemplate.exchange(
                any(URI.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(DummyReturnClass.class)
                )
        ).thenReturn(serverResponse);
        DummyReturnClass result = httpTask.doInBackground();
        Assert.assertSame(null, result);
    }
}