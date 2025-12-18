package org.example.service;

import org.example.DTO.TransactionViewDTO;

import org.example.model.Account;
import org.example.model.Transaction;


import java.util.LinkedList;
import java.util.List;


public class InMemoryTransactionService implements TransactionService {


    @Override
    public List<TransactionViewDTO> readAllTransactionsFromAccount(Account account) {
        List<Transaction> transaction=account.getTransactions();

        List<TransactionViewDTO> transactionViewDTO=new LinkedList<TransactionViewDTO>();
        for(Transaction t:transaction){
            transactionViewDTO.add(TransactionViewDTO.mapper(t));
        }

        return transactionViewDTO;
    }

    @Override
    public Transaction createTransaction(Transaction transaction,Account account) {
        account.addTransaction(transaction);
        return transaction;
    }

    @Override
    public Transaction readTransactionById(Long transactionId, Account account) {
        return account.getTransactions().stream().
                        filter(t ->
                        t.getTransactionId().equals(transactionId))
                        .findFirst().orElse(null);
    }
}
