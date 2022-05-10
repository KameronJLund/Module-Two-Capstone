package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService extends ServiceBase<AuthenticatedUser>
{
    public UserService(String BASE_URL)
    {
        super(BASE_URL);
    }

    public Long getAccountId(String username)
    {
        Long accountId = null;

        try
        {
            String url = BASE_URL + username;

            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(), Account.class);
            accountId = response.getBody().getId();
        }

        catch (RestClientException | NullPointerException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }

        return accountId;
    }

    public List<String> getAllUsernames()
    {
        List<String> usernames = new ArrayList<>();

        try
        {
            String url = BASE_URL + "username";

            ResponseEntity<String[]> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(), String[].class);
            if (response.getBody() != null) usernames = Arrays.asList(response.getBody());
        }

        catch (RestClientException | NullPointerException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }

        return usernames;
    }



}
