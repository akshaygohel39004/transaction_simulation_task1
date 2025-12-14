package org.example.model;

import java.util.Objects;

public class RequestTransaction {
    private Long requestTransactionId;
    private Transaction transaction;
    private RequestStatus requestStatus;

    public RequestTransaction() {}

    public RequestTransaction(Long requestTransactionId, Transaction transactionId, RequestStatus requestStatus) {
        this.requestTransactionId = requestTransactionId;
        this.transaction = transactionId;
        this.requestStatus = requestStatus;
    }

    public Long getRequestTransactionId() { return requestTransactionId; }
    public void setRequestTransactionId(Long requestTransactionId) { this.requestTransactionId = requestTransactionId; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public RequestStatus getRequestStatus() { return requestStatus; }
    public void setRequestStatus(RequestStatus requestStatus) { this.requestStatus = requestStatus; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestTransaction)) return false;
        RequestTransaction that = (RequestTransaction) o;
        return Objects.equals(requestTransactionId, that.requestTransactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestTransactionId);
    }

    @Override
    public String toString() {
        return "RequestTransaction{" +
                "requestTransactionId=" + requestTransactionId +
                ", transactionId=" + transaction +
                ", requestStatus=" + requestStatus +
                '}';
    }
}
