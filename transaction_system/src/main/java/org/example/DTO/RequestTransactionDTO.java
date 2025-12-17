package org.example.DTO;

import org.example.model.Account;
import org.example.model.RequestTransaction;
import org.example.model.User;

import java.util.List;
import java.util.Map;

public class RequestTransactionDTO {
    private final Map<Account, List<RequestTransaction>> requestTransactionsSender;
    private final Map<Account, List<RequestTransaction>> requestTransactionsReceiver;

    public RequestTransactionDTO(Map<Account, List<RequestTransaction>> requestTransactionsSender, Map<Account, List<RequestTransaction>> requestTransactionsReceiver) {
        this.requestTransactionsSender = requestTransactionsSender;
        this.requestTransactionsReceiver = requestTransactionsReceiver;
    }

    public Map<Account, List<RequestTransaction>> getRequestTransactionsSender() {
        return requestTransactionsSender;
    }

    public Map<Account, List<RequestTransaction>> getRequestTransactionsReceiver() {
        return requestTransactionsReceiver;
    }


}
