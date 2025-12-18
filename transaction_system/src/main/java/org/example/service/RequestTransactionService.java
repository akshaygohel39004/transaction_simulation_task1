package org.example.service;
import org.example.DTO.RequestTransactionDTO;
import org.example.model.*;
import java.util.List;
import java.util.Map;

public interface RequestTransactionService {

   public void CreateRequestTransaction(RequestTransactionDTO requestTransactionDTO,RequestTransaction requestTransaction);
    public void UpdateRequestTransaction(RequestTransactionDTO requestTransactionDTO,RequestTransaction requestTransaction);

}
