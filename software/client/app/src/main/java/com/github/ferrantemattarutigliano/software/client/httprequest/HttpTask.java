package com.github.ferrantemattarutigliano.software.client.httprequest;

import android.os.AsyncTask;
import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.util.Information;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public abstract class HttpTask<O> extends AsyncTask<Void, Void, O>{
    private AsyncResponse<O> asyncResponse;
    private Class<O> outputClass;
    private ParameterizedTypeReference<O> outputCollection;
    private HttpInformationContainer httpInformationContainer;
    private HttpRequestStatus resultType;
    private int errorCode;

    //this is tricky: we need to pass the actual output class to the constructor
    //because Java can't handle generic type at runtime, only compile time
    public HttpTask(Class<O> outputClass, AsyncResponse<O> asyncResponse) {
        this(asyncResponse);
        this.outputClass = outputClass;
    }

    //this constructor is necessary to receive java generics
    public HttpTask(ParameterizedTypeReference<O> outputCollection, AsyncResponse<O> asyncResponse) {
        this(asyncResponse);
        this.outputCollection = outputCollection;

    }

    private HttpTask(AsyncResponse<O> asyncResponse) {
        super();
        this.asyncResponse = asyncResponse;
        resultType = HttpRequestStatus.CREATED;
    }

    @Override
    protected O doInBackground(Void... voids) {
        resultType = HttpRequestStatus.RUNNING;

        //if this is an @Authorized task, add the login token to the request header
        if(this.getClass().isAnnotationPresent(Authorized.class)){
            AuthorizationToken.addAuthorizationToken(httpInformationContainer);
        }
        final RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(HttpConstant.CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(HttpConstant.READ_TIMEOUT);
        restTemplate.setRequestFactory(requestFactory);

        if(httpInformationContainer == null) throw new RuntimeException();

        HttpMethod httpMethod = httpInformationContainer.getHttpMethod();
        String path = httpInformationContainer.getPath();
        Object parameter = httpInformationContainer.getParameter();
        HttpHeaders headers = httpInformationContainer.getHttpHeaders();
        //post request must have a body
        if(httpMethod.equals(HttpMethod.POST) && parameter == null)
            throw new RuntimeException(Information.HTTP_POST_PARAMETERS_NOT_FOUND.toString());
        if((outputClass == null && outputCollection == null))
            throw new RuntimeException();
        try {
            return sendRequest(restTemplate, httpMethod, path, parameter, headers);
        } catch (ResourceAccessException e) {
            Log.e("HTTP_TIMEOUT", "Http connection timeout");
            resultType = HttpRequestStatus.TIMEOUT;
        } catch (HttpMessageNotReadableException e) {
            Log.e("HTTP_JSON_ERROR", "Http json error: " + e.getMessage());
            resultType = HttpRequestStatus.JSON_FAIL;
        } catch (HttpServerErrorException e){
            Log.e("HTTP_SERVER_EX", "Server exception: " + e.getMessage());
            resultType = HttpRequestStatus.SERVER_FAIL;
            errorCode = e.getStatusCode().value();
        } catch (HttpClientErrorException e){
            Log.e("HTTP_CLIENT_EX", "Client exception: " + e.getMessage());
            resultType = HttpRequestStatus.CLIENT_FAIL;
            errorCode = e.getStatusCode().value();
        } catch (RuntimeException e) {
            Log.e("HTTP_RUNTIME_EX", "Runtime http exception: " + e.getMessage());
            resultType = HttpRequestStatus.RUNTIME_FAIL;
        } catch (Exception e) {
            Log.e("HTTP_GENERIC_EX", "Http exception: " + e.getMessage());
            resultType = HttpRequestStatus.FAILED;
        }
        return null;
    }

    private O sendRequest(RestTemplate restTemplate, HttpMethod httpMethod, String accessPath, Object parameter, HttpHeaders headers){
        HttpEntity<Object> requestEntity = new HttpEntity<>(parameter, headers);
        ResponseEntity<O> responseEntity;
        String serverDomain = HttpConstant.SERVER_DOMAIN;
        String serverPath = "http://".concat(serverDomain).concat("/" + accessPath);
        if(outputClass != null)
            responseEntity = restTemplate.exchange(serverPath, httpMethod, requestEntity, outputClass);
        else
            responseEntity = restTemplate.exchange(serverPath, httpMethod, requestEntity, outputCollection);
        resultType = HttpRequestStatus.SUCCESS;
        O result = null;
        try{
            result = responseEntity.getBody();
            //if this is an @Authentication task add the login token to AuthorizationToken.
            //this token will be included in every request header that implements @Authorized
            if(this.getClass().isAnnotationPresent(Authentication.class)){
                String tokenName = this.getClass().getAnnotation(Authentication.class).tokenName();
                HttpHeaders responseHeaders = responseEntity.getHeaders();
                String tokenValue = responseHeaders.get(tokenName).get(0);
                AuthorizationToken.setAuthName(tokenName);
                AuthorizationToken.setAuthToken(tokenValue);
            }
        }
        catch (Exception e){
            Log.e("HTTP_BODY_NULL", "Failed to fetch http response body.");
        }
        return result;
    }

    @Override
    protected void onPostExecute(O o) {
        if(asyncResponse == null)
            throw new RuntimeException(Information.ASYNC_RESPONSE_NOT_FOUND.toString());

        try {
            switch (resultType){
                case TIMEOUT:
                    asyncResponse.taskFailMessage(HttpOutputMessage.TIMEOUT.toString());
                    return;
                case JSON_FAIL:
                    asyncResponse.taskFailMessage(HttpOutputMessage.JSON_FAIL.toString());
                    return;
                case SERVER_FAIL:
                    asyncResponse.taskFailMessage(HttpOutputMessage.SERVER_FAIL.toString());
                    break;
                case CLIENT_FAIL:
                    asyncResponse.taskFailMessage(HttpOutputMessage.CLIENT_FAIL.toString());
                    break;
                case RUNTIME_FAIL:
                    asyncResponse.taskFailMessage(HttpOutputMessage.RUNTIME_FAIL.toString());
                    return;
                case FAILED:
                    asyncResponse.taskFailMessage(HttpOutputMessage.UNKNOWN_FAIL.toString());
                    return;
                case SUCCESS:
                    asyncResponse.taskFinish(o);
            }
        }
        catch (NullPointerException e){
            e.fillInStackTrace();
            Log.e("NULL_POINTER_EX", e.getMessage());
        }
    }

    public void setHttpInformationContainer(HttpInformationContainer httpInformationContainer) {
        this.httpInformationContainer = httpInformationContainer;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
