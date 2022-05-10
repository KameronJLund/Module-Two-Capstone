package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests
{
    private static final Transfer TRANSFER_1 = new Transfer(3001L, 2L, 2L,  2001L, 2002L, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_2 = new Transfer(3002L, 2L, 1L,  2002L, 2003L, BigDecimal.valueOf(50.00));
    private static final Transfer TRANSFER_3 = new Transfer(3003L, 2L, 3L,  2001L, 2003L, BigDecimal.valueOf(75.00));

    private JdbcTransferDao transferDao;

    @Before
    public void setup() {transferDao = new JdbcTransferDao(dataSource);}

    // get all transfers

    // get transfer by id

    //get all transfer by status

    // get all transfers by type

    // can tarnsfer

    // create send

    // create request

    // confirm request

    // deny request

    private void assertTransfersMatch(String message, Transfer expected, Transfer actual)
    {
        Assert.assertEquals(message, expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(message, expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(message, expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(message, expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(message, expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(message, expected.getAmount(), actual.getAmount());

    }
}
