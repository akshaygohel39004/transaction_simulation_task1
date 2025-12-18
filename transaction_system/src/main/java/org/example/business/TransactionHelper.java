package org.example.business;

import org.example.exceptions.*;
import org.example.model.*;
import org.example.stats.PaymentStatsRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TransactionHelper {
    private final PaymentStatsRouter statsRouter;
    private final PaymentService paymentService;
    Logger logger = LoggerFactory.getLogger(TransactionHelper.class);

    public TransactionHelper(PaymentStatsRouter statsRouter,PaymentService paymentService) {
        this.statsRouter = statsRouter;
        this.paymentService = paymentService;
    }

    private Transaction getTransaction(Account sender, Account receiver, Double Amount){
        Transaction transaction = new Transaction();
        transaction.setTransactionInitiationTime(LocalDateTime.now());
        transaction.setPaymentService(paymentService);
        transaction.setTransactionId(Utility.generateUniqueLongId());
        transaction.setStatus(TransactionStatus.INITIATION);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(Amount);
        return transaction;
    }

    public  void doSelfTransaction(Account account, Double Amount,TransactionType transactionType) {
        Double updatedAccountBalance,currentAccountBalance;
        currentAccountBalance=updatedAccountBalance=account.getAccountBalance();
        Transaction transaction =getTransaction(account,account,Amount);
        if(transactionType.equals(TransactionType.CREDIT)){
            transaction.setTransactionType(TransactionType.CREDIT);
            updatedAccountBalance=currentAccountBalance+Amount;
            logger.info("Current Account Balance is "+currentAccountBalance);
            logger.info("Updated Account Balance: "+updatedAccountBalance);
        }
        else{
            transaction.setTransactionType(TransactionType.WITHDRAW);
            try{
                if(currentAccountBalance<Amount){

                    throw new Exception("Insufficient Balance");
                }
                else{
                    updatedAccountBalance=currentAccountBalance-Amount;

                }
            }
            catch (Exception e){
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
                account.addTransaction(transaction);
                transaction.setStatus(TransactionStatus.FAILED);
                statsRouter.submit(transaction);
                return;
            }
        }
        account.setAccountBalance(updatedAccountBalance);
        account.addTransaction(transaction);
        transaction.setStatus(TransactionStatus.COMPLETED);
        statsRouter.submit(transaction);
    }

    public  void doTransferTransaction(Account sender, Account receiver, Double Amount, TransactionStatus transactionStatus){
        Transaction transaction=getTransaction(sender,receiver,Amount);
        //* critical section starts here
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(Amount);
        try{
            synchronized (sender){
                synchronized (receiver){
                    Double currentAccountBalanceSender=sender.getAccountBalance();
                    Double currentAccountBalanceReceiver=receiver.getAccountBalance();
                    if(transactionStatus.equals(TransactionStatus.FAILED)){
                        ExceptionsCenter.general("Request has to cancel, failed transactio");
                    }
                    if(currentAccountBalanceSender<Amount){

                        throw new Exception("Insufficient Balance");
                    }
                    else{

                        currentAccountBalanceSender=currentAccountBalanceSender-Amount;
                        sender.setAccountBalance(currentAccountBalanceSender);
                        currentAccountBalanceReceiver=currentAccountBalanceReceiver+Amount;
                        receiver.setAccountBalance(currentAccountBalanceReceiver);
                    }
                }
            }



        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //if there is insufficient balance means sender has failed transaction
            sender.addTransaction(transaction);
            receiver.addTransaction(transaction);
            transaction.setStatus(TransactionStatus.FAILED);
            statsRouter.submit(transaction);
            return;
        }
        //critical section ends here
        sender.addTransaction(transaction);
        receiver.addTransaction(transaction);
        transaction.setStatus(TransactionStatus.COMPLETED);
        statsRouter.submit(transaction);
    }


    public RequestTransaction doRequestTransaction(Account sender, Account receiver, Double Amount) {
        Transaction transaction=getTransaction(sender, receiver, Amount);
        RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setTransaction(transaction);
        requestTransaction.setRequestTransactionId(Utility.generateUniqueLongId());
        requestTransaction.setRequestStatus(RequestStatus.PENDING);
        return requestTransaction;
    }
}
