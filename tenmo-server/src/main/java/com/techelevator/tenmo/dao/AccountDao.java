package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao
{
    List<Account> accountList();

    BigDecimal findBalance(Long id);

    Account getAccount(Long id);

    Account getAccountByUser(Long userId);

}
