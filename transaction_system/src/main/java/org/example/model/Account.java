package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account implements Comparable<Account>{

    private Long accountId;
    private String accountNumber; // 10 digit
    private AccountType accountType;
    private Double accountBalance;
    private final List<Transaction> transactions = new ArrayList<>();



    public Account() {}

    public Account(Long accountId, String accountNumber,Double accountBalance, AccountType accountType) {

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public List<Transaction> getTransactions() { return transactions; }

    public void addTransaction(Transaction tx) {
        if (tx == null) return;
        if (!transactions.contains(tx)) transactions.add(tx);
    }

    public void removeTransaction(Transaction tx) {
        if (tx == null) return;
        transactions.remove(tx);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountBalance='" + accountBalance + '\'' +
                ", accountType=" + accountType +
                ", transactionsCount=" + transactions.size() +
                '}';
    }

    @Override
    public int compareTo(Account o) {
        return o.getAccountId().compareTo(this.getAccountId());
    }
}
