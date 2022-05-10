package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService extends ServiceBase<AuthenticatedUser>
{

    public TransferService(String BASE_URL) {
        super(BASE_URL);
    }

    public List<Transfer> getHistory(AuthenticatedUser user)
    {
        List<Transfer> history = new ArrayList<>();

        try
        {
            String url = BASE_URL + "user/" + user.getUser().getId();

            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(user), Transfer[].class);
            if (response.getBody() != null) history = Arrays.asList(response.getBody());
        }
        catch (RestClientException | NullPointerException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return history;
    }

    public List<Transfer> getRelevantPendingRequests(AuthenticatedUser user)
    {
        List<Transfer> history = new ArrayList<>();

        try
        {
            String url = BASE_URL + "requests/pending";

            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(), Transfer[].class);
            if (response.getBody() != null) history = Arrays.asList(response.getBody());
        }
        catch (RestClientException | NullPointerException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return history;
    }

    public Transfer getTransfer(Long transferId)
    {
        Transfer transfer = new Transfer();

        try
        {
            String url = BASE_URL + transferId;

            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(), Transfer.class);
            transfer = response.getBody();
        }
        catch (RestClientException | NullPointerException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public void makeTransfer
    (
         Long transferTypeId,
         Long transferStatusId,
         Long accountFrom,
         Long accountTo,
         BigDecimal amount
    )
    {
        Transfer transfer = new Transfer(transferTypeId, transferStatusId, accountFrom, accountTo, amount);

        try
        {
            String url = BASE_URL;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(getAuthToken());
            HttpEntity<Transfer> entity = new HttpEntity<> (transfer, headers);

            restTemplate.
                    postForObject(url, entity, Void.class);
        }
        catch (RestClientException | NullPointerException e)
        {
            BasicLogger.log(e.getMessage());
        }
    }


    public void confirmRequest(Transfer transfer)
    {
        try
        {
            String url = BASE_URL + "requests/confirm/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(getAuthToken());
            HttpEntity<Transfer> entity = new HttpEntity<> (transfer, headers);

            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        }
        catch (RestClientException | NullPointerException e)
        {
            BasicLogger.log(e.getMessage());
        }
    }

    public void denyRequest(Long transferId)
    {
        try
        {
            String url = BASE_URL + "requests/deny/" + transferId;

            restTemplate.exchange(url, HttpMethod.PUT, getAuthEntity(), Void.class);
        }
        catch (RestClientException | NullPointerException e)
        {
            BasicLogger.log(e.getMessage());
        }
    }
}
