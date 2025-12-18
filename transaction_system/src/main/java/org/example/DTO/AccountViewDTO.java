package org.example.DTO;

public class AccountViewDTO {

    private final String userName;
    private final String accountNumber;
    private final Double balance;

    public AccountViewDTO(String userName, String accountNumber, Double balance) {
        this.userName = userName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }



    public String getUserName() {
        return userName;
    }



    public Double getBalance() {
        return balance;
    }



    @Override
    public String toString() {
        return "AccountViewDTO{" +
                "userName='" + userName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}
