package org.example.DTO;

import org.example.model.*;

import java.time.LocalDateTime;

public class TransactionViewDTO {
    private Long transactionId;
    private Account receiverAccount;
    private Account senderAccount;
    private TransactionType transactionType;         // DEBITE / CREDIT
    private LocalDateTime transactionInitiationTime;
    private TransactionStatus status;
    private PaymentService paymentService;            // MobileGateway, CardProcessor, UPI
    private Double amount;
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

    public static TransactionViewDTO mapper(Transaction transaction) {
        return new TransactionViewDTO(transaction.getReceiver(), transaction.getTransactionId(),transaction.getSender(),transaction.getTransactionType(),transaction.getTransactionInitiationTime(),transaction.getStatus(),transaction.getPaymentService(),transaction.getAmount());
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionInitiationTime() {
        return transactionInitiationTime;
    }

    public void setTransactionInitiationTime(LocalDateTime transactionInitiationTime) {
        this.transactionInitiationTime = transactionInitiationTime;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
                '}';
    }
}
