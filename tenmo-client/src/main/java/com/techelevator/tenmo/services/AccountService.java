package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import io.cucumber.java.en_old.Ac;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.Bidi;

public class AccountService extends ServiceBase<AuthenticatedUser> {

    public AccountService(String BASE_URL) {
        super(BASE_URL);
    }


    public BigDecimal getBalance(AuthenticatedUser user)
    {
        BigDecimal balance = new BigDecimal(0);

        try
        {
            String url2 = BASE_URL + "user/balance";
            ResponseEntity<BigDecimal> response2 = restTemplate.exchange(url2, HttpMethod.GET, getAuthEntity(user), BigDecimal.class);

            balance = response2.getBody();
        } catch (RestClientException | NullPointerException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Long getAccountId(AuthenticatedUser user)
    {
        Long balance = null;

        try
        {
            String url = BASE_URL + "user/account_id";
            ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(user), Long.class);
            balance = response.getBody();
        } catch (RestClientException | NullPointerException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public String getUsername(Long accountId)
    {
        String username = null;

        try
        {
            String url = BASE_URL + "user/username/" + accountId ;

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(), String.class);
            username = response.getBody();
        }

        catch (RestClientException | NullPointerException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }

        return username;
    }

}
