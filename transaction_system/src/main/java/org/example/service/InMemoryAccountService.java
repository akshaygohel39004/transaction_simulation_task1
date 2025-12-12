package org.example.service;

import org.example.model.Account;
import org.example.model.AccountType;
import org.example.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * View-only AccountService using UserService as the source of truth
 * for user → accounts relationship.
 *
 * Dummy accounts are seeded by adding them directly to users.
 */
public class InMemoryAccountService implements AccountService {

    private final UserService userService;

    public InMemoryAccountService(UserService userService) {
        this.userService = userService;
        seedSampleData();
    }

    @Override
    public List<Account> readAllAccounts() {
        List<Account> all = new ArrayList<>();
        for (User u : userService.readAllUsers()) {
            all.addAll(u.getAccounts());
        }
        return all;
    }

    @Override
    public Optional<Account> readAccountById(Long accountId) {
        if (accountId == null) return Optional.empty();

        return userService.readAllUsers().stream()
                .flatMap(u -> u.getAccounts().stream())
                .filter(a -> accountId.equals(a.getAccountId()))
                .findFirst();
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        if (userId == null) return Collections.emptyList();

        return userService.readUserById(userId)
                .map(User::getAccounts)
                .orElse(Collections.emptyList());
    }

    /* ---------- Add dummy accounts here ---------- */
    private void seedSampleData() {
        // fetch existing seeded users from UserService
        Optional<User> u1Opt = userService.readUserById(1L);
        Optional<User> u2Opt = userService.readUserById(2L);

        if (u1Opt.isPresent()) {
            User u1 = u1Opt.get();
            u1.addAccount(new Account(100L, "1000000001", AccountType.SAVING));
            u1.addAccount(new Account(101L, "1000000002", AccountType.CHECKING));
        }

        if (u2Opt.isPresent()) {
            User u2 = u2Opt.get();
            u2.addAccount(new Account(102L, "1000000003", AccountType.SAVING));
        }
    }
}
