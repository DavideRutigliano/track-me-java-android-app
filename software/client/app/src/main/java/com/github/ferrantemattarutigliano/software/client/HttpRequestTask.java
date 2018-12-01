package com.github.ferrantemattarutigliano.software.client;

import android.os.AsyncTask;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class HttpRequestTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        try{
            // The connection URL
            String url = "https://ajax.googleapis.com/ajax/" +
                    "services/search/web?v=1.0&q={query}";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = restTemplate.getForObject(url, String.class, "Android");
            return result;
        }
        catch (Exception e){}
        return null;
    }
}
