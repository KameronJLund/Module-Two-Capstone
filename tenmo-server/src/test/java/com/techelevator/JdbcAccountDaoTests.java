package com.techelevator;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests
{
    private static final Account ACCOUNT_1 = new Account(1L, 1L, BigDecimal.valueOf(100.00));
    private static final Account ACCOUNT_2 = new Account(2L, 2L, BigDecimal.valueOf(150.00));

    private JdbcAccountDao accountDao;

    @Before
    public void setup() {accountDao = new JdbcAccountDao(dataSource);}

    @Test
    public void getAccount_returnsCorrectAccount_for_id()
    {
        //arrange
        //act
        Account account1 = accountDao.getAccount(1L);
        Account account2 = accountDao.getAccount(2L);

        String message = "Because account ID should return correct account details";
        //assert
        assertAccountsMatch(message, ACCOUNT_1, account1);
        assertAccountsMatch(message, ACCOUNT_2, account2);
    }

    @Test
    public void getAccount_returns_null_when_id_not_found()
    {
        //arrange
        //act
        Account account5 = accountDao.getAccount(5L);
        Account account6 = accountDao.getAccount(6L);

        String message = "Because the timesheet does not exist";

        //assert
        Assert.assertNull(message, account5);
        Assert.assertNull(message, account6);
    }

    private void assertAccountsMatch(String message, Account expected, Account actual)
    {
        Assert.assertEquals(message, expected.getId(), actual.getId());
        Assert.assertEquals(message, expected.getUserId(), actual.getUserId());
        Assert.assertEquals(message, expected.getBalance(), actual.getBalance());
    }
}
