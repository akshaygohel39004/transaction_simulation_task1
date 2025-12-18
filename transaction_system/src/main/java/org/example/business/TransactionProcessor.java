package org.example.business;

import org.example.model.Account;
import org.example.model.RequestTransaction;
import org.example.model.TransactionStatus;
import org.example.model.TransactionType;

public interface TransactionProcessor {
    public void SelfTransaction(Account account, Double Amount, TransactionType type);
    public void transferTransaction(Account sender, Account receiver, Double Amount, TransactionStatus transactionStatus);
    public RequestTransaction RequestTransaction(Account sender, Account receiver, Double Amount);
}
