package org.example.business;

import org.example.model.Account;
import org.example.model.RequestTransaction;

public interface IMakeTransactions {
    public void SelfTransaction(Account account,Double Amount,boolean isDeposit);
    public void transferTransaction(Account sender,Account receiver,Double Amount,boolean willCancel);
    public RequestTransaction RequestTransaction(Account sender, Account receiver, Double Amount);
}
