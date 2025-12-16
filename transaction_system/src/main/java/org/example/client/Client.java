package org.example.client;

import org.example.DTO.AccountViewDTO;
import org.example.DTO.RequestTransactionDTO;
import org.example.DTO.TransactionViewDTO;
import org.example.business.IMakeTransactions;
import org.example.business.*;
import org.example.exceptions.ExceptionsCenter;
import org.example.model.*;
import org.example.service.*;

import java.util.*;

public class Client {
    //stores user indexing on id
    private static final Map<Long,User> users = new HashMap<>();



    //this all are services
    private final UserService userService = new InMemoryUserService();
    private final AccountService accountService=new InMemoryAccountService(userService);

    //tracking of authentication
    private User logedinUser;

    public Client(){

        seedSampleData();
    }
    private void seedSampleData() {
        // lightweight sample users (IDs chosen arbitrarily)
        User u1 = new User(1L, "Akshay", "Akshay@gmail.com", "9999999991");
        User u2 = new User(2L, "Raj", "Raj@yahho.com", "9999999992");


        u1.addAccount(new Account(100L, "1",5000D, AccountType.SAVING));
        u1.addAccount(new Account(101L, "2", 20000D,AccountType.CHECKING));
        addUser(u1);

        u2.addAccount(new Account(102L, "3", 40005D,AccountType.SAVING));
        addUser(u2);

    }

    public void start() {
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Choose what you want to be do");
            System.out.println("1.Login");
            System.out.println("2.Account List");
            System.out.println("3.See my all accounts");
            System.out.println("4.Deposit to your account");
            System.out.println("5.Withdraw from your account ");
            System.out.println("6.see all transactions of my account");
            System.out.println("7.do transaction to other account");
            System.out.println("8.initiate transaction request ");
            System.out.println("9.Handle Transaction requests");
            System.out.println("10.log out");
            System.out.println("11.EXIT");

            int Choose;

            Choose=sc.nextInt();
            switch (Choose){
                case 1:
                    try {
                        login();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    listAllAccount();
                    break;
                case 3:
                    try {
                        printMyAccountDetails();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 10:
                    logout();
                case 11:
                    return;

                default:
                    System.out.println("Wrong choice");

            }

        }}

    private void addUser(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("user and userId must not be null");
        }
        users.put(user.getUserId(), user);
    }
    private void login() throws Exception {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter Username");
        String username = scanner.next();
        userService.readAllUsers(users).stream().filter(u -> u.getUserName().equals(username)).findFirst().ifPresent(u -> logedinUser=u);
        if(logedinUser==null){
            ExceptionsCenter.throwNotFound("User");
        }
        System.out.println("Your login done");
    }

    private boolean authenticate(){
        return logedinUser == null;
    }

    private void listAllAccount(){
        List<AccountViewDTO> accountViewDTOList=accountService.readAllAccounts(users);
        for(AccountViewDTO accountViewDTO : accountViewDTOList){
            System.out.println(accountViewDTO);
        }
    }

    private void printMyAccountDetails() throws Exception {
        if(authenticate()){
            ExceptionsCenter.throwUnAuthorized();
        }
        List<Account> AccountList=logedinUser.getAccounts();
        for(Account account:AccountList){
            System.out.println(account);
        }
    }




    private void logout(){
        logedinUser=null;
    }
}