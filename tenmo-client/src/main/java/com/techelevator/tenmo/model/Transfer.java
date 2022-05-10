package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer
{
    private Long transferId;
    private Long transferTypeId;
    private Long transferStatusId;
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;

    public Transfer() {
    }

    public Transfer(Long transferId, Long transferTypeId, Long transferStatusId, Long accountFrom, Long accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
    public Transfer( Long transferTypeId, Long transferStatusId,Long accountFrom, Long accountTo, BigDecimal amount)
    {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String transferType()
    {
        if (transferTypeId == 1) {
            return "Request";
        }
        return "Send";
    }

    public String transferStatus()
    {
        if (transferStatusId == 1) {
            return "Pending";
        }
        else if (transferStatusId == 2)
        {
            return "Approved";
        }
        return "Rejected";
    }

    @Override
    public String toString()
    {
        return "User{" +
                "transfer id=" + transferId +
                ", transfer type=" + transferTypeId +
                ", transfer status=" + transferStatusId +
                ", account from='" + accountFrom +
                ", account to='" + accountTo +
                ", amount= $" + amount +
                '}';
    }
    //can transfer
}
