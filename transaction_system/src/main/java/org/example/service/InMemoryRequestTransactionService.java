package org.example.service;

import org.example.DTO.RequestTransactionDTO;
import org.example.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryRequestTransactionService implements RequestTransactionService {

    @Override
    public void CreateRequestTransaction(RequestTransactionDTO requestTransactionDTO, RequestTransaction requestTransaction) {
        Transaction transaction = requestTransaction.getTransaction();
        Account receiver=transaction.getReceiver();
        Account sender=transaction.getSender();
        List<RequestTransaction> senderRequestTransactions=requestTransactionDTO.getRequestTransactionsSender().get(sender);
        List<RequestTransaction> receiverRequestTransactions=requestTransactionDTO.getRequestTransactionsReceiver().get(receiver);

        //here above can be null because if user is newly created
        if(senderRequestTransactions==null){
            senderRequestTransactions=new ArrayList<>();
            requestTransactionDTO.getRequestTransactionsSender().put(sender,senderRequestTransactions);
        }
        if(receiverRequestTransactions==null){
            receiverRequestTransactions=new ArrayList<>();
            requestTransactionDTO.getRequestTransactionsReceiver().put(receiver,receiverRequestTransactions);
        }

        senderRequestTransactions.add(requestTransaction);
        receiverRequestTransactions.add(requestTransaction);

    }

    @Override
    public void UpdateRequestTransaction(RequestTransactionDTO requestTransactionDTO, RequestTransaction requestTransaction) {

        Transaction transaction=requestTransaction.getTransaction();

        //firstly remove requestTransactionEntry from list

        //this contains list of request transaction side of request get
        List<RequestTransaction> requestTransactionReceivers=requestTransactionDTO.getRequestTransactionsReceiver().get(transaction.getSender());
        if(requestTransactionReceivers!=null)
            requestTransactionReceivers.remove(requestTransaction);


        //this  contains list of request transaction side of request sent
        List<RequestTransaction> requestTransactionSenders=requestTransactionDTO.getRequestTransactionsSender().get(transaction.getReceiver());
        if(requestTransactionSenders!=null)
            requestTransactionSenders.remove(requestTransaction);


    }
}
