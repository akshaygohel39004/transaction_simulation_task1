package org.example.service;

import org.example.DTO.TransactionViewDTO;
import org.example.model.Account;
import org.example.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionService {


    List<TransactionViewDTO> readAllTransactionsFromAccount(Account account);


    Transaction createTransaction(Transaction transaction,Account account);


    Transaction readTransactionById(Long transactionId,Account account);


}
