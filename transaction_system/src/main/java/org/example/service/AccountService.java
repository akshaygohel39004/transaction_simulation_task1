package org.example.service;

import org.example.DTO.AccountViewDTO;
import org.example.exceptions.GeneralException;
import org.example.exceptions.NotFoundException;
import org.example.model.Account;
import org.example.model.User;

import java.util.List;
import java.util.Map;


public interface AccountService {

    List<AccountViewDTO> readAllAccounts(Map<Long, User> users) throws GeneralException;

    Account readAccountByAccountNumber(Map<String, Account> accountMap, String accountNumber) throws Exception;
}