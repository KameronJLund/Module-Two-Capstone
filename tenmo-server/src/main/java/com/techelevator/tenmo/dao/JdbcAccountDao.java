package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao
{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Account> accountList()
    {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id " +
                ", user_id " +
                ", balance " +
                "FROM account ";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while(rows.next())
        {
            Account account = mapRowToAccount(rows);
            accounts.add(account);
        }

        return accounts;
    }

    @Override
    public BigDecimal findBalance(Long userId)
    {
        String sql = "SELECT balance " +
                "FROM account as a " +
                "JOIN tenmo_user as u " +
                "ON u.user_id = a.user_id " +
                "WHERE u.user_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, userId);

        BigDecimal balance = null;
        if(row.next())
        {
            balance = row.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public Account getAccount(Long id)
    {
        String sql = "SELECT account_id " +
                ", user_id " +
                ", balance " +
                "FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);

        Account account = null;
        if(row.next())
        {
            account = mapRowToAccount(row);
        }
        return account;
    }

    @Override
    public Account getAccountByUser(Long userId)
    {
        String sql = "SELECT account_id " +
                ", user_id " +
                ", balance " +
                "FROM account " +
                "WHERE user_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, userId);

        Account account = null;
        if(row.next())
        {
            account = mapRowToAccount(row);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet row)
    {
        Account account;

        Long id = row.getLong("account_id");
        Long userId = row.getLong("user_id");
        BigDecimal balance = row.getBigDecimal("balance");

        account = new Account(id, userId, balance);

        return account;
    }
}
