package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long transactionId;
    private TransactionType transactionType;         // DEBITE / CREDIT
    private LocalDateTime transactionInitiationTime;
    private TransactionStatus status;
    private PaymentService paymentService;            // MobileGateway, CardProcessor, UPI
    private Account sender;
    private Account receiver;
    private Double amount;
    public Transaction() {}

    private Transaction(Builder builder) {
        this.transactionId = builder.transactionId;
        this.transactionType = builder.transactionType;
        this.transactionInitiationTime = builder.transactionInitiationTime;
        this.status = builder.status;
        this.paymentService = builder.paymentService;
        this.sender = builder.sender;
        this.receiver = builder.receiver;
        this.amount = builder.amount;

    }
    public Transaction(Long transactionId,
                       TransactionType transactionType,
                       LocalDateTime transactionInitiationTime,
                       TransactionStatus status,
                       PaymentService paymentService,
                       Account sender,
                       Account receiver,
                       Double amount) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.transactionInitiationTime = transactionInitiationTime;
        this.status=status;
        this.paymentService = paymentService;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public LocalDateTime getTransactionInitiationTime() { return transactionInitiationTime; }
    public void setTransactionInitiationTime(LocalDateTime transactionInitiationTime) {
        this.transactionInitiationTime = transactionInitiationTime;
    }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public PaymentService getPaymentService() { return paymentService; }
    public void setPaymentService(PaymentService paymentService) { this.paymentService = paymentService; }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", transactionInitiationTime=" + transactionInitiationTime +
                ", status=" + status +
                ", paymentService=" + paymentService +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", amount=" + amount +
                '}';
    }

    public static class Builder{
        private Long transactionId;
        private TransactionType transactionType;         // DEBITE / CREDIT
        private LocalDateTime transactionInitiationTime=LocalDateTime.now();
        private TransactionStatus status;
        private PaymentService paymentService;            // MobileGateway, CardProcessor, UPI
        private Account sender;
        private Account receiver;
        private Double amount=0D;

        public Builder(){

        }

        public Builder setTransactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder setTransactionInitiationTime(LocalDateTime transactionInitiationTime) {
            this.transactionInitiationTime = transactionInitiationTime;
            return this;
        }

        public Builder setStatus(TransactionStatus status) {
            this.status = status;
            return this;
        }

        public Builder setPaymentService(PaymentService paymentService) {
            this.paymentService = paymentService;
            return this;
        }

        public Builder setSender(Account sender) {
            this.sender = sender;
            return this;
        }

        public Builder setAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setReceiver(Account receiver) {
            this.receiver = receiver;
            return this;
        }

        public Transaction Build(){
            return new Transaction(this);
        }
    }
}
