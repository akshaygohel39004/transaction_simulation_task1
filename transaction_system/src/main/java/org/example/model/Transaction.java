package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long transactionId;
    private TransactionType transactionType;         // DEBITE / CREDIT
    private LocalDateTime transactionInitiationTime;
    private String status;
    private PaymentService paymentService;            // MobileGateway, CardProcessor, UPI

    public Transaction() {}

    public Transaction(Long transactionId,
                       TransactionType transactionType,
                       LocalDateTime transactionInitiationTime,
                       String status,
                       PaymentService paymentService) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.transactionInitiationTime = transactionInitiationTime;
        this.status = status;
        this.paymentService = paymentService;
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public LocalDateTime getTransactionInitiationTime() { return transactionInitiationTime; }
    public void setTransactionInitiationTime(LocalDateTime transactionInitiationTime) {
        this.transactionInitiationTime = transactionInitiationTime;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public PaymentService getPaymentService() { return paymentService; }
    public void setPaymentService(PaymentService paymentService) { this.paymentService = paymentService; }

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

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", transactionInitiationTime=" + transactionInitiationTime +
                ", status='" + status + '\'' +
                ", paymentService=" + paymentService +
                '}';
    }
}
