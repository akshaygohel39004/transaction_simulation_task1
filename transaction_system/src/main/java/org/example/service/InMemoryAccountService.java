package org.example.service;

import org.example.DTO.AccountViewDTO;
import org.example.model.Account;
import org.example.model.AccountType;
import org.example.model.User;

import java.util.*;


public class InMemoryAccountService implements AccountService {

    private final UserService userService;

    public InMemoryAccountService(UserService userService) {
        this.userService = userService;


    }

    @Override
    public List<AccountViewDTO> readAllAccounts(Map<Long,User> users) {
        List<AccountViewDTO> all = new ArrayList<>();
        for (User u : userService.readAllUsers(users)) {
           for(Account a : u.getAccounts()) {
               all.add(new AccountViewDTO(u.getUserName(),a.getAccountNumber(),a.getAccountBalance()));
           }
        }
        return all;
    }

    @Override
    public Account readAccountByAccountNumber(Map<Long, User> users, String accountNumber) {
        for(User u:users.values()){
            for(Account a:u.getAccounts()){
                if(a.getAccountNumber().equals(accountNumber)){
                    return a;
                }
            }
        }

        return null;
    }


}
