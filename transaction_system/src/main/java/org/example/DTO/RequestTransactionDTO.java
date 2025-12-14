package org.example.DTO;

import org.example.model.Account;
import org.example.model.RequestTransaction;
import org.example.model.User;

import java.util.List;
import java.util.Map;

public class RequestTransactionDTO {
    private Map<Account, List<RequestTransaction>> requestTransactionsSender;
    private Map<Account, List<RequestTransaction>> requestTransactionsReceiver;

    public Map<Account, List<RequestTransaction>> getRequestTransactionsSender() {
        return requestTransactionsSender;
    }

    public void setRequestTransactionsSender(Map<Account, List<RequestTransaction>> requestTransactionsSender) {
        this.requestTransactionsSender = requestTransactionsSender;
    }

    public Map<Account, List<RequestTransaction>> getRequestTransactionsReceiver() {
        return requestTransactionsReceiver;
    }

    public void setRequestTransactionsReceiver(Map<Account, List<RequestTransaction>> requestTransactionsReceiver) {
        this.requestTransactionsReceiver = requestTransactionsReceiver;
    }
}
