package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcTransferDao implements TransferDao
{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transfer> getAllTransfers(Long userId)
    {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id " +
                ", t.transfer_type_id " +
                ", t.transfer_status_id " +
                ", t.account_from " +
                ", t.account_to " +
                ", t.amount " +
                " FROM transfer AS t" +
                " JOIN account AS a ON a.account_id = t.account_from " +
                " OR a.account_id = t.account_to" +
                " JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                " WHERE tu.user_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId);

        while(rows.next())
        {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Transfer getTransferById(Long transferId)
    {
        String sql = "SELECT transfer_id " +
                ", transfer_type_id " +
                ", transfer_status_id " +
                ", account_from " +
                ", account_to " +
                ", amount " +
                " FROM transfer" +
                " WHERE transfer_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, transferId);

        if(rows.next())
        {
            return mapRowToTransfer(rows);
        }
        return null;
    }

    @Override
    public List<Transfer> getAllTransfersByStatus(Long userId, Long transferStatusId)
    {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id " +
                ", t.transfer_type_id " +
                ", t.transfer_status_id " +
                ", t.account_from " +
                ", t.account_to " +
                ", t.amount " +
                " FROM transfer AS t" +
                " JOIN account AS a ON a.account_id = transfer.account_from " +
                " AND a.account_id = transfer.account_to" +
                " JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                " WHERE tu.user_id = ?" +
                " AND t.transfer_status_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId, transferStatusId);

        while(rows.next())
        {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByType(Long userId, Long transferTypeId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id " +
                ", t.transfer_type_id " +
                ", t.transfer_status_id " +
                ", t.account_from " +
                ", t.account_to " +
                ", t.amount " +
                " FROM transfer AS t" +
                " JOIN account AS a ON a.account_id = transfer.account_from " +
                " AND a.account_id = transfer.account_to" +
                " JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                " WHERE tu.user_id = ?" +
                " AND t.transfer_type_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId, transferTypeId);

        while(rows.next())
        {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Boolean canTransfer(Long accountFrom, Long accountTo, BigDecimal amount)
    {
        JdbcAccountDao jdbcAccountDao;
        BigDecimal balance = null;
        Boolean distinctAccounts = !Objects.equals(accountFrom, accountTo);
        Boolean validAmount = amount.compareTo(BigDecimal.valueOf(0)) > 0;

        String sql = "SELECT account_id " +
                ", user_id " +
                ", balance " +
                "FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountFrom);

        Boolean accountFromValid = false;
        if(row.next())
        {
            balance = row.getBigDecimal("balance");
            accountFromValid = true;
        }

        SqlRowSet row2 = jdbcTemplate.queryForRowSet(sql, accountTo);

        Boolean accountToValid = false;
        if(row2.next())
        {
            accountToValid = true;
        }

        Boolean validBalance = amount.compareTo(balance) <= 0;

        return distinctAccounts && accountFromValid && accountToValid && validAmount && validBalance;
    }

    @Transactional
    public void createSend
            (
                    Long accountFrom,
                    Long accountTo,
                    BigDecimal amount
            )
    {
        String sql = "INSERT INTO transfer( transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES ( 2, 2, ?, ?, ?)";
        String sqlAccountFrom = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?;";
        String sqlAccountTo = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";

        jdbcTemplate.update(sql, accountFrom, accountTo, amount);
        jdbcTemplate.update(sqlAccountFrom, amount, accountFrom);
        jdbcTemplate.update(sqlAccountTo, amount, accountTo);
    }

    @Override
    public void createRequest
            (
                    Long accountFrom,
                    Long accountTo,
                    BigDecimal amount
            )
    {
        String sql = "INSERT INTO transfer( transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES ( 1, 1, ?, ?, ?)";
//

        jdbcTemplate.update(sql, accountFrom, accountTo, amount);
    }

    @Override
    @Transactional
    public void confirmRequest (Transfer transfer)
    {

        String sql = "UPDATE transfer " +
                "SET transfer_status_id = 2 " +
                "WHERE transfer_id = ?;";
        String sqlAccountFrom = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?;";
        String sqlAccountTo = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";


        jdbcTemplate.update(sqlAccountFrom, transfer.getAmount(), transfer.getAccountTo());
        jdbcTemplate.update(sqlAccountTo, transfer.getAmount(), transfer.getAccountFrom());
        jdbcTemplate.update(sql, transfer.getTransferId());
    }

    @Override
    @Transactional
    public void denyRequest (Long transferId)
    {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = 3 " +
                "WHERE transfer_id = ?;";

        jdbcTemplate.update(sql, transferId);
    }

    private Transfer mapRowToTransfer(SqlRowSet row)
    {
        Transfer transfers;

        Long transferId = row.getLong("transfer_id");
        Long transferTypeId = row.getLong("transfer_type_id");
        Long transferStatusId = row.getLong("transfer_status_id");
        Long accountFrom = row.getLong("account_from");
        Long accountTo = row.getLong("account_to");
        BigDecimal amount = row.getBigDecimal("amount");

        transfers = new Transfer(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, amount);

        return transfers;
    }


}
