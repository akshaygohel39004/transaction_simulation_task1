//this class is only focused for testing of synchronization works properly while transferTransaction happens

//here we creates three accounts c1,c2,c3.
//c1 and c2 are sender into c3
//we try both done at same time and we use sync mechanism inside transferTransaction method
//lets check is it work or not

//then i will attach static file mobilegateway_stats.txt when you can check statics only for that second when I have did transaction.


package org.example.test;
import org.example.DTO.AccountViewDTO;

import org.example.business.IMakeTransactions;
import org.example.business.*;
import org.example.exceptions.ThrowExcpetions;
import org.example.model.*;
import org.example.service.*;
import org.example.stats.PaymentStatsRouter;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DemoClient {
    //stores user indexing on id
    private static final Map<Long,User> users = new HashMap<>();
    //stores requestTransaction based on sender indexing
    private final Map<Account, List<RequestTransaction>> requestTransactionsSender =new TreeMap<>();
    //stores requestTransaction based on receiver indexing
    private final Map<Account, List<RequestTransaction>> requestTransactionsReceiver =new TreeMap<>();

    private final PaymentStatsRouter statsRouter;


    //this all are services
    private final UserService userService = new InMemoryUserService();
    private final AccountService accountService=new InMemoryAccountService(userService);

    //tracking of authentication
    private User logedinUser;



    public DemoClient(PaymentStatsRouter statsRouter) {
        this.statsRouter = statsRouter;
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
        try {
            transferTransaction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
            ThrowExcpetions.throwNotFound("User");
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
            ThrowExcpetions.throwUnAuthorized();
        }
        List<Account> AccountList=logedinUser.getAccounts();
        for(Account account:AccountList){
            System.out.println(account);
        }
    }

    private void transferTransaction() throws Exception {

        ExecutorService executorService= Executors.newFixedThreadPool(2);
        IMakeTransactions makeTransactions=new MakeTransactionThroughMobileGateWay(statsRouter);

        Account senderC1= new ArrayList<>(users.values()).get(0).getAccounts().get(0);
        Account senderC2=new ArrayList<>(users.values()).get(0).getAccounts().get(1);
        Account receiverC2=new ArrayList<>(users.values()).get(1).getAccounts().get(0);
        System.out.println(receiverC2.getAccountBalance());
        executorService.submit(()->{
            makeTransactions.transferTransaction(senderC1,receiverC2,500D,false);
        });

        executorService.submit(()->{
            makeTransactions.transferTransaction(senderC2,receiverC2,500D,false);
        });

        executorService.awaitTermination(2, TimeUnit.SECONDS);
        executorService.shutdown();
        System.out.println(receiverC2.getAccountBalance());
    }






}
