package com.github.ferrantemattarutigliano.software.client.httprequest;

import android.os.AsyncTask;
import android.util.Log;

import com.github.ferrantemattarutigliano.software.client.Information;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public abstract class HttpTask<O> extends AsyncTask<Void, Void, O> {
    private AsyncResponse<O> asyncResponse;
    private Class<O> outputClass;
    private HttpParameterContainer httpParameterContainer;
    private HttpRequestStatus resultType;

    //this is tricky: we need to pass the actual output class to the constructor
    //because Java can't handle generic type at runtime, only compile time
    public HttpTask(Class<O> outputClass, AsyncResponse<O> asyncResponse) {
        super();
        this.outputClass = outputClass;
        this.asyncResponse = asyncResponse;
        resultType = HttpRequestStatus.CREATED;
    }

    @Override
    protected O doInBackground(Void... voids) {
        resultType = HttpRequestStatus.RUNNING;
        final RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(HttpConstant.CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(HttpConstant.READ_TIMEOUT);
        restTemplate.setRequestFactory(requestFactory);
        O result = null;
        try{
            if(httpParameterContainer == null) throw new RuntimeException();
            HttpRequestType type = httpParameterContainer.getHttpRequestType();
            String path = httpParameterContainer.getPath();
            Object postParameters = httpParameterContainer.getPostParameters();

            if(type.equals(HttpRequestType.POST)
                && postParameters== null) throw new RuntimeException(Information.HTTP_POST_PARAMETERS_NOT_FOUND.toString());
            try {
                switch (type) { //TODO add put, delete (if necessary)
                    case GET:
                        result = restTemplate.getForObject(HttpConstant.SERVER_PATH + path, outputClass);
                        break;
                    case POST:
                        result = restTemplate.postForObject(HttpConstant.SERVER_PATH + path, postParameters, outputClass);
                        break;
                }
            }
            catch (ResourceAccessException e){
                Log.e("HTTP_TIMEOUT", "Http connection timeout");
                resultType = HttpRequestStatus.TIMEOUT;
                return null;
            }
            catch (HttpMessageNotReadableException e){
                Log.e("HTTP_JSON_ERROR", "Http json error");
                resultType = HttpRequestStatus.JSON_FAIL;
                return null;
            }
            catch (RuntimeException e){
                Log.e("HTTP_RUNTIME_EX", "Runtime http exception: " + e.getMessage());
                resultType = HttpRequestStatus.FAILED;
                return null;
            }
        }
        catch (Exception e){
            e.fillInStackTrace();
            resultType = HttpRequestStatus.FAILED;
        }
        resultType = HttpRequestStatus.SUCCESS;
        return result;
    }

    @Override
    protected void onPostExecute(O o) {
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
                    return;
            }
        }
        catch (NullPointerException e){
            throw new RuntimeException(Information.ASYNC_RESPONSE_NOT_FOUND.toString());
        }
    }

    public void setHttpParameterContainer(HttpParameterContainer httpParameterContainer) {
        this.httpParameterContainer = httpParameterContainer;
    }

    public HttpRequestStatus getResultType() {
        return resultType;
    }
}
