package org.example.service;

import org.example.DTO.AccountViewDTO;
import org.example.exceptions.ExceptionsCenter;
import org.example.exceptions.GeneralException;
import org.example.exceptions.NotFoundException;
import org.example.model.Account;
import org.example.model.User;

import java.lang.classfile.instruction.ExceptionCatch;
import java.util.*;


public class InMemoryAccountService implements AccountService {

    private final UserService userService;

    public InMemoryAccountService(UserService userService) {
        this.userService = userService;


    }

    @Override
    public List<AccountViewDTO> readAllAccounts(Map<Long,User> users) throws GeneralException {
        List<AccountViewDTO> all = new ArrayList<>();
        userService.readAllUsers(users).stream().forEach((u)->{
            u.getAccounts().stream().forEach((a)->{
                all.add(new AccountViewDTO(u.getUserName(),a.getAccountNumber(),a.getAccountBalance()));
            });
        });

        return all;
    }

    @Override
    public Account readAccountByAccountNumber(Map<String, Account> accountMap, String accountNumber) throws Exception{
        Account account=accountMap.get(accountNumber);
        if(account==null){
             ExceptionsCenter.throwNotFound("Account");
        }
        return account;
    }


}
