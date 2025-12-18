package org.example.client;

import org.example.DTO.AccountViewDTO;
import org.example.DTO.RequestTransactionDTO;
import org.example.DTO.TransactionViewDTO;
import org.example.business.IMakeTransactions;
import org.example.business.*;
import org.example.exceptions.ThrowExcpetions;
import org.example.model.*;
import org.example.service.*;
import org.example.stats.PaymentStatsRouter;

import java.util.*;

public class Client {
    //stores user indexing on id
    private static final Map<Long,User> users = new HashMap<>();
    //store  accountNumber as index
    private static final Map<String,Account> accounts = new HashMap<>();
    //stores requestTransaction based on sender indexing
    private final Map<Account, List<RequestTransaction>> requestTransactionsSender =new TreeMap<>();
    //stores requestTransaction based on receiver indexing
    private final Map<Account, List<RequestTransaction>> requestTransactionsReceiver =new TreeMap<>();
    static Scanner  scanner=new Scanner(System.in);


    //this all are services
    private final UserService userService = new InMemoryUserService();
    private final TransactionService transactionService = new InMemoryTransactionService();
    private final RequestTransactionService requestTransactionService = new InMemoryRequestTransactionService();
    private final AccountService accountService=new InMemoryAccountService(userService);

    //tracking of authentication
    private User logedinUser;

    public Client(PaymentStatsRouter statsRouter){
        this.statsRouter = statsRouter;
        seedSampleData();
    }
    private void seedSampleData() {
        // lightweight sample users (IDs chosen arbitrarily)
        User u1 = new User(1L, "Akshay", "Akshay@gmail.com", "9999999991");
        User u2 = new User(2L, "Raj", "Raj@yahho.com", "9999999992");


        u1.addAccount(new Account(100L, "1",5000D, AccountType.SAVING));
        u1.addAccount(new Account(101L, "2", 20000D,AccountType.CHECKING));
        accounts.put(u1.getAccounts().get(0).getAccountNumber(),u1.getAccounts().get(0));
        accounts.put(u1.getAccounts().get(1).getAccountNumber(),u1.getAccounts().get(1));
        addUser(u1);

        u2.addAccount(new Account(102L, "3", 40005D,AccountType.SAVING));
        addUser(u2);
        accounts.put(u2.getAccounts().get(0).getAccountNumber(),u2.getAccounts().get(0));

    }

    public TransactionProcessor paymentServiceType(){
        System.out.println("Select payment service from following options");
        System.out.println("1.CardProcessor");
        System.out.println("2.UPI");
        System.out.println("Rest.MobileGateWay");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();

        return switch (choice) {
            case 1 -> TransactionFactory.getTransactionPaymentService(PaymentService.CardProcessor);
            case 2 -> TransactionFactory.getTransactionPaymentService(PaymentService.UPI);
            default -> TransactionFactory.getTransactionPaymentService(PaymentService.MobileGateway);
        };
    }


    public void start() {
        while(true){

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

            Choose=scanner.nextInt();
            switch (Choose){
                case 1:
                    try {
                        login();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        listAllUserAccounts();
                    } catch (GeneralException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        printMyAccountDetails();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        selfTransaction(TransactionType.CREDIT);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        selfTransaction(TransactionType.WITHDRAW);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        myAllTransactions();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("All transactions");
                    break;
                case 7:
                    try{
                        transferTransaction();
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        requestTransaction();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 9:
                    try {
                        handleTransaction();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 10:
                    logout();
                    break;

                case 11:
                    return;

                default:
                    System.out.println("Wrong choice");

            }

        }}
    private Account getMyAccount() throws Exception {
        //ask about which account you want to use

        printMyAccountDetails();
        //basic details input request sender account number , request receiver account number,amount
        System.out.println("Enter your Account Number");
        String myAccountNumber=scanner.next();

        return accountService.readAccountByAccountNumber(accounts,myAccountNumber);
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

        String username = scanner.nextLine();
        userService.readAllUsers(users).stream().filter(u -> u.getUserName().equals(username)).findFirst().ifPresent(u -> logedinUser=u);
        if(logedinUser==null){
            ExceptionsCenter.throwNotFound("User");
        }
        System.out.println("Your login done");
    }

    private boolean isAuthenticate(){
        return logedinUser == null;
    }

    private void listAllUserAccounts() throws GeneralException {
        List<AccountViewDTO> accountViewDTOList=accountService.readAllAccounts(users);
        for(AccountViewDTO accountViewDTO : accountViewDTOList){
            System.out.println(accountViewDTO);
        }
    }

    private void printMyAccountDetails() throws Exception {
        if(isAuthenticate()){
            ExceptionsCenter.throwUnAuthorized();
        }
        List<Account> AccountList=logedinUser.getAccounts();
        for(Account account:AccountList){
            System.out.println(account);
        }
    }

    private void selfTransaction(TransactionType transactionType) throws Exception {
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        System.out.println("Enter Account Number");
        String accountNumber=scanner.next();

        Account account= accountService.readAccountByAccountNumber(accounts,accountNumber);
        if(account==null){
            ExceptionsCenter.throwNotFound("Account");
        }
        //if user wants to withdraw(not deposit) then it should be logged in and Account should be belonged to his/her account
            if(transactionType==TransactionType.WITHDRAW){
            if(isAuthenticate()){
                ExceptionsCenter.throwUnAuthorized();
            }
            if(!logedinUser.getAccounts().contains(account)){
                ExceptionsCenter.throwNotFound("Account");
            }
        }

        System.out.println("Enter Amount");
        Double amount=scanner.nextDouble();

        TransactionProcessor makeTransactions=paymentServiceType();

        makeTransactions.SelfTransaction(account,amount,transactionType);

    }

    private void myAllTransactions() throws Exception {
        if(isAuthenticate()){
            ExceptionsCenter.throwUnAuthorized();
        }
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        System.out.println("Enter Account Number");
        String accountNumber=scanner.next();

        Account account= accountService.readAccountByAccountNumber(accounts,accountNumber);
        if(account==null){
            ExceptionsCenter.throwNotFound("Account");
        }

        List<TransactionViewDTO> transactionViewDTOList= transactionService.readAllTransactionsFromAccount(account);

        for (TransactionViewDTO transactionViewDTO :transactionViewDTOList) {
            System.out.println(transactionViewDTO);
        }
    }

    private void transferTransaction() throws Exception {
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        System.out.println("Enter your Account Number");
        String myAccountNumber=scanner.next();

        Account myAccount= accountService.readAccountByAccountNumber(accounts,myAccountNumber);
        if(myAccount==null){
            ExceptionsCenter.throwNotFound("sender's account");
        }
        listAllUserAccounts();
        System.out.println("Enter receiver's account number");
        String receiverAccountNumber=scanner.next();
        Account receiverAccount= accountService.readAccountByAccountNumber(accounts,receiverAccountNumber);
        if(receiverAccount==null){
            ExceptionsCenter.throwNotFound("receiver's account");
        }
        if(receiverAccount.equals(myAccount)){
            ExceptionsCenter.general("You can't transfer money in same account");
        }
        System.out.println("Enter Amount");
        Double amount=scanner.nextDouble();
        TransactionProcessor makeTransactions=paymentServiceType();

        makeTransactions.transferTransaction(myAccount,receiverAccount,amount,TransactionStatus.PENDING);
    }

    private void requestTransaction() throws Exception {
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        //basic details input request sender account number , request receiver account number,amount
        System.out.println("Enter your Account Number");
        String myAccountNumber=scanner.next();

        Account myAccount= accountService.readAccountByAccountNumber(accounts,myAccountNumber);
        if(myAccount==null){
            ExceptionsCenter.throwNotFound("sender's account");
        }
        listAllUserAccounts();
        System.out.println("Enter request receiver's account number");
        String receiverAccountNumber=scanner.next();
        Account receiverAccount= accountService.readAccountByAccountNumber(accounts,receiverAccountNumber);
        if(receiverAccount==null){
            ExceptionsCenter.throwNotFound("request receiver's account");
        }
        if(receiverAccount.equals(myAccount)){
            ExceptionsCenter.general("You can't request money in same account");
        }
        System.out.println("Enter Amount");
        Double amount=scanner.nextDouble();

        TransactionProcessor makeTransactions=paymentServiceType();


        //gives requestTransaction object
        RequestTransaction requestTransaction=makeTransactions.RequestTransaction(myAccount,receiverAccount,amount);

        //creating request transaction DTO
        RequestTransactionDTO requestTransactionDTO=new RequestTransactionDTO(requestTransactionsSender,requestTransactionsReceiver);

        //added requestTransaction into list maintaining inside client itself
        requestTransactionService.CreateRequestTransaction(requestTransactionDTO,requestTransaction);

    }

    //this function is use for handleTransaction
    //we need to maintain loged in Account in caller function so we return it here
    private Account  printTransactionRequest() throws Exception {
        if(isAuthenticate()){
            ExceptionsCenter.throwUnAuthorized();
        }

        Account myAccount=getMyAccount();
        if(myAccount==null){
            ExceptionsCenter.throwNotFound("sender's account");
        }


        //requests which are received
        List<RequestTransaction> requestTransaction1=requestTransactionsReceiver.get(myAccount);

        if(requestTransaction1==null){
            requestTransaction1=new ArrayList<>();
        }


        System.out.println("requests which you received\n");
        if(!requestTransaction1.isEmpty())
            System.out.println("Request Id             Account Number     Amount");
        else
        {
            System.out.println("No records found");
            return null;
        }

        requestTransaction1.stream().forEach((requestTransaction) -> {
            System.out.println(requestTransaction.getRequestTransactionId()+"           "
                    +requestTransaction.getTransaction().getSender().getAccountNumber()+"            "
                    +requestTransaction.getTransaction().getAmount()
            );
        });

        return myAccount;
    }



    private void handleTransaction() throws Exception {
        Account selectedAccount=printTransactionRequest();
        if(selectedAccount==null){
            return;
        }
        System.out.println("above is your requests.\nChoose options");
        System.out.println("1.Back");
        System.out.println("2.handle request");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();
        if(choice==1){
            return;
        }
        System.out.println("Enter RequestId");
        Long requestId=sc.nextLong();
        RequestTransaction requestTransaction =
                requestTransactionsReceiver.get(selectedAccount)
                        .stream()
                        .filter(req -> req.getRequestTransactionId().equals(requestId))
                        .findFirst()
                        .orElse(null);


        if(requestTransaction==null){
            ExceptionsCenter.throwNotFound("request not found");
        }

        System.out.println("Enter 1 to Accpet.\nEnter else to Reject");
        choice=sc.nextInt();
        //creating request transaction DTO
        RequestTransactionDTO requestTransactionDTO=new RequestTransactionDTO(requestTransactionsSender,requestTransactionsReceiver);

        requestTransactionService.UpdateRequestTransaction(requestTransactionDTO,requestTransaction);
        Transaction transaction=requestTransaction.getTransaction();
        Account receiver=transaction.getSender(); //here transaction.getSender is request sender so in real transaction it will become receiver of money
        Account sender=transaction.getReceiver(); //here transaction.getReceiver is request receiver so in real transaction he/she will become sender of money
        Double amount=transaction.getAmount();
        TransactionProcessor makeTransactions=paymentServiceType();

        if(choice==1){
            makeTransactions.transferTransaction(sender,receiver,amount, TransactionStatus.PENDING);
        }
        else{
            makeTransactions.transferTransaction(sender,receiver,amount, TransactionStatus.FAILED);
        }


    }


    private void logout(){
        logedinUser=null;
    }
}
