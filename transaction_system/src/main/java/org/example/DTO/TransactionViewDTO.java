package org.example.DTO;

import org.example.model.Account;
import org.example.model.PaymentService;
import org.example.model.TransactionStatus;
import org.example.model.TransactionType;

import java.time.LocalDateTime;

public class TransactionViewDTO {
    private final Long transactionId;
    private final Account receiverAccount;
    private final Account senderAccount;
    private final TransactionType transactionType;         // DEBITE / CREDIT
    private final LocalDateTime transactionInitiationTime;
    private final TransactionStatus status;
    private final PaymentService paymentService;            // MobileGateway, CardProcessor, UPI
    private final Double amount;
    public TransactionViewDTO(Account receiverAccount, Long transactionId, Account senderAccount, TransactionType transactionType, LocalDateTime transactionInitiationTime, TransactionStatus status, PaymentService paymentService, Double amount) {
        this.receiverAccount = receiverAccount;
        this.transactionId = transactionId;
        this.senderAccount = senderAccount;
        this.transactionType = transactionType;
        this.transactionInitiationTime = transactionInitiationTime;
        this.status = status;
        this.paymentService = paymentService;
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionInitiationTime() {
        return transactionInitiationTime;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public Double getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return "TransactionViewDTO{" +
                "transactionId=" + transactionId +
                (receiverAccount!=null?
                ", receiverAccount=" + receiverAccount:"") +
                (senderAccount!=null?
                ", senderAccount=" + senderAccount:"") +
                ", transactionType=" + transactionType +
                ", transactionInitiationTime=" + transactionInitiationTime +
                ", status=" + status +
                ", paymentService=" + paymentService +
                ", amount=" + amount +
                '}';
    }
}
