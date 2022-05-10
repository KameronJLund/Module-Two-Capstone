package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

public abstract class ServiceBase<T>
{
    private static String authToken  = null;
    protected final String BASE_URL;
    protected final RestTemplate restTemplate;

    public ServiceBase(String BASE_URL) {
        this.BASE_URL = BASE_URL;
        this.restTemplate = new RestTemplate();
    }

    public static void setAuthToken(String authToken)
    {
        ServiceBase.authToken = authToken;
    }

    public static String getAuthToken()
    {
        return authToken;
    }

    protected HttpEntity<Void> getAuthEntity()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    protected HttpEntity<T> getAuthEntity(T body)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<T>(body, headers);
    }
}
