package org.example.service;

import org.example.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    /**
     * Return all accounts.
     */
    List<Account> readAllAccounts();

    /**
     * Return account details by id.
     */
    Optional<Account> readAccountById(Long accountId);

    /**
     * Return all accounts belonging to a user.
     */
    List<Account> getAccountsByUserId(Long userId);
}
