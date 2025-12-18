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
    //stores requestTransaction based on sender indexing
    private final Map<Account, List<RequestTransaction>> requestTransactionsSender =new TreeMap<>();
    //stores requestTransaction based on receiver indexing
    private final Map<Account, List<RequestTransaction>> requestTransactionsReceiver =new TreeMap<>();

    private final PaymentStatsRouter statsRouter;


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
        addUser(u1);

        u2.addAccount(new Account(102L, "3", 40005D,AccountType.SAVING));
        addUser(u2);

    }
    public IMakeTransactions paymentServiceType(){
        System.out.println("Select payment service from following options");
        System.out.println("1.CardProcessor");
        System.out.println("2.UPI");
        System.out.println("Rest.MobileGateWay");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();

        return switch (choice) {
            case 1 -> TransactionFactory.getTransactionPaymentService(PaymentService.CardProcessor,statsRouter);
            case 2 -> TransactionFactory.getTransactionPaymentService(PaymentService.UPI,statsRouter);
            default -> TransactionFactory.getTransactionPaymentService(PaymentService.MobileGateway,statsRouter);
        };
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
                case 4:
                    try {
                        selfTransaction(true);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        selfTransaction(false);
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

    private void selfTransaction(boolean isDeposit) throws Exception {
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        System.out.println("Enter Account Number");
        String accountNumber=scanner.next();

        Account account= accountService.readAccountByAccountNumber(users,accountNumber);
        if(account==null){
            ThrowExcpetions.throwNotFound("Account");
        }
        //if user wants to withdraw(not deposit) then it should be logged in and Account should be belonged to his/her account
        if(!isDeposit){
            if(authenticate()){
                ThrowExcpetions.throwUnAuthorized();
            }
            if(!logedinUser.getAccounts().contains(account)){
                ThrowExcpetions.throwNotFound("Account");
            }
        }

        System.out.println("Enter Amount");
        Double amount=scanner.nextDouble();

        IMakeTransactions makeTransactions=paymentServiceType();

        makeTransactions.SelfTransaction(account,amount,isDeposit);

    }

    private void myAllTransactions() throws Exception {
        if(authenticate()){
            ThrowExcpetions.throwUnAuthorized();
        }
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        System.out.println("Enter Account Number");
        String accountNumber=scanner.next();

        Account account= accountService.readAccountByAccountNumber(users,accountNumber);
        if(account==null){
            ThrowExcpetions.throwNotFound("Account");
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

        Account myAccount= accountService.readAccountByAccountNumber(users,myAccountNumber);
        if(myAccount==null){
            ThrowExcpetions.throwNotFound("sender's account");
        }
        listAllAccount();
        System.out.println("Enter receiver's account number");
        String receiverAccountNumber=scanner.next();
        Account receiverAccount= accountService.readAccountByAccountNumber(users,receiverAccountNumber);
        if(receiverAccount==null){
            ThrowExcpetions.throwNotFound("receiver's account");
        }
        if(receiverAccount.equals(myAccount)){
            ThrowExcpetions.general("You can't transfer money in same account");
        }
        System.out.println("Enter Amount");
        Double amount=scanner.nextDouble();
        IMakeTransactions makeTransactions=paymentServiceType();

        makeTransactions.transferTransaction(myAccount,receiverAccount,amount,false);
    }

    private void requestTransaction() throws Exception {
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        //basic details input request sender account number , request receiver account number,amount
        System.out.println("Enter your Account Number");
        String myAccountNumber=scanner.next();

        Account myAccount= accountService.readAccountByAccountNumber(users,myAccountNumber);
        if(myAccount==null){
            ThrowExcpetions.throwNotFound("sender's account");
        }
        listAllAccount();
        System.out.println("Enter request receiver's account number");
        String receiverAccountNumber=scanner.next();
        Account receiverAccount= accountService.readAccountByAccountNumber(users,receiverAccountNumber);
        if(receiverAccount==null){
            ThrowExcpetions.throwNotFound("request receiver's account");
        }
        if(receiverAccount.equals(myAccount)){
            ThrowExcpetions.general("You can't request money in same account");
        }
        System.out.println("Enter Amount");
        Double amount=scanner.nextDouble();

        IMakeTransactions makeTransactions=paymentServiceType();


        //gives requestTransaction object
        RequestTransaction requestTransaction=makeTransactions.RequestTransaction(myAccount,receiverAccount,amount);

        //creating request transaction DTO
        RequestTransactionDTO requestTransactionDTO=new RequestTransactionDTO().mapper(requestTransactionsSender,requestTransactionsReceiver);

        //added requestTransaction into list maintaining inside client itself
        requestTransactionService.CreateRequestTransaction(requestTransactionDTO,requestTransaction);

    }

    //this function is use for handleTransaction
    //we need to maintain loged in Account in caller function so we return it here
    private Account  printTransactionRequest() throws Exception {
        if(authenticate()){
            ThrowExcpetions.throwUnAuthorized();
        }
        //ask about which account you want to use
        Scanner scanner=new Scanner(System.in);
        printMyAccountDetails();
        //basic details input request sender account number , request receiver account number,amount
        System.out.println("Enter your Account Number");
        String myAccountNumber=scanner.next();

        Account myAccount= accountService.readAccountByAccountNumber(users,myAccountNumber);
        if(myAccount==null){
            ThrowExcpetions.throwNotFound("sender's account");
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

        for(RequestTransaction requestTransaction:requestTransaction1){
            System.out.println(requestTransaction.getRequestTransactionId()+"           "
                    +requestTransaction.getTransaction().getSender().getAccountNumber()+"            "
                    +requestTransaction.getTransaction().getAmount()
            );
        }
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
            ThrowExcpetions.throwNotFound("request not found");
        }

        System.out.println("Enter 1 to Accpet.\nEnter else to Reject");
        choice=sc.nextInt();
        //creating request transaction DTO
        RequestTransactionDTO requestTransactionDTO=new RequestTransactionDTO();
        requestTransactionDTO.setRequestTransactionsReceiver(requestTransactionsReceiver);
        requestTransactionDTO.setRequestTransactionsSender(requestTransactionsSender);
        requestTransactionService.UpdateRequestTransaction(requestTransactionDTO,requestTransaction);
        Transaction transaction=requestTransaction.getTransaction();
        Account receiver=transaction.getSender(); //here transaction.getSender is request sender so in real transaction it will become receiver of money
        Account sender=transaction.getReceiver(); //here transaction.getReceiver is request receiver so in real transaction he/she will become sender of money
        Double amount=transaction.getAmount();
        IMakeTransactions makeTransactions=paymentServiceType();

        makeTransactions.transferTransaction(sender,receiver,amount, choice != 1);


    }


    private void logout(){
        logedinUser=null;
    }
}
