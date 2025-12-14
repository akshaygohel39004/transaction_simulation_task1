package org.example.service;

import org.example.DTO.AccountViewDTO;
import org.example.model.Account;
import org.example.model.User;

import java.util.List;
import java.util.Map;


public interface AccountService {

    List<AccountViewDTO> readAllAccounts(Map<Long, User> users);
    Account readAccountByAccountNumber(Map<Long, User> users, String accountNumber);
}
