package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private Long userId;
    private String userName;
    private String email;
    private String phone;
    private final List<Account> accounts = new ArrayList<>();

    public User() {}

    public User(Long userId, String userName, String email, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Account> getAccounts() { return accounts; }

    public void addAccount(Account account) {
        if (account == null) return;
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        if (account == null) return;
        accounts.remove(account);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", accountsCount=" + accounts.size() +
                '}';
    }
}
