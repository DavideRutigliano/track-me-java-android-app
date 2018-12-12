package com.github.ferrantemattarutigliano.software.client.httprequest;

import android.os.AsyncTask;
import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.Information;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public abstract class HttpTask<O> extends AsyncTask<Void, Void, O> {
    private AsyncResponse<O> asyncResponse;
    private Class<O> outputClass;
    private ParameterizedTypeReference<O> outputCollection;
    private HttpInformationContainer httpInformationContainer;
    private HttpRequestStatus resultType;

    //this is tricky: we need to pass the actual output class to the constructor
    //because Java can't handle generic type at runtime, only compile time
    public HttpTask(Class<O> outputClass, AsyncResponse<O> asyncResponse) {
        super();
        this.outputClass = outputClass;
        this.asyncResponse = asyncResponse;
        resultType = HttpRequestStatus.CREATED;
    }

    //this constructor is necessary to receive java generics
    public HttpTask(ParameterizedTypeReference<O> outputCollection, AsyncResponse<O> asyncResponse) {
        super();
        this.asyncResponse = asyncResponse;
        this.outputCollection = outputCollection;
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
            Log.e("HTTP_JSON_ERROR", "Http json error");
            e.fillInStackTrace();
            resultType = HttpRequestStatus.JSON_FAIL;
        } catch (RuntimeException e) {
            Log.e("HTTP_RUNTIME_EX", "Runtime http exception: " + e.getMessage());
            e.fillInStackTrace();
            resultType = HttpRequestStatus.RUNTIME_FAIL;
        }
        catch (Exception e) {
            Log.e("HTTP_EX", "Http exception: " + e.getMessage());
            e.fillInStackTrace();
            resultType = HttpRequestStatus.FAILED;
        }
        return null;
    }

    private O sendRequest(RestTemplate restTemplate, HttpMethod httpMethod, String path, Object parameter, HttpHeaders headers){
        HttpEntity<Object> requestEntity = new HttpEntity<>(parameter, headers);
        ResponseEntity<O> responseEntity;
        responseEntity = restTemplate.exchange(HttpConstant.SERVER_PATH + path, httpMethod, requestEntity, outputClass);
        resultType = HttpRequestStatus.SUCCESS;
        O result = null; //todo add error handling
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
            Log.e("HTTP_BODY_NULL", "Failed to fetch http response body."); //todo rewrite this
        }
        return result;
    }

    @Override
    protected void onPostExecute(O o) {
        if(asyncResponse == null)
            throw new RuntimeException(Information.ASYNC_RESPONSE_NOT_FOUND.toString());

        try {
            switch (resultType){
                case RUNTIME_FAIL:
                    asyncResponse.taskFailMessage("Http runtime failure");
                    return;
                case JSON_FAIL:
                    asyncResponse.taskFailMessage("Failed to convert json");
                    return;
                case FAILED:
                    asyncResponse.taskFailMessage("Failed for unknown reason");
                    return;
                case TIMEOUT:
                    asyncResponse.taskFailMessage("Connection timeout");
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
}
