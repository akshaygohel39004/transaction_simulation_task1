package org.example.business;

import org.example.exceptions.ExceptionsCenter;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;

public class TransactionProcessorThroughUPI implements TransactionProcessor {

    Logger logger = LoggerFactory.getLogger(TransactionProcessorThroughUPI.class);
    public TransactionProcessorThroughUPI() {

    }

    //for deposit/withdraw into account
    private Transaction getTransaction(Account sender,Account receiver,Double amount){
        Transaction transaction = (new Transaction.Builder())
                .setTransactionId(Utility.generateUniqueLongId())
                .setPaymentService(PaymentService.UPI)
                .setStatus(TransactionStatus.PENDING)
                .setTransactionType(TransactionType.TRANSFER)
                .setSender(sender)
                .setReceiver(receiver)
                .setAmount(amount).Build();

        return transaction;
    }

    @Override
    public void SelfTransaction(Account account, Double Amount, TransactionType transactionType) {
        Double updatedAccountBalance,currentAccountBalance;
        currentAccountBalance=updatedAccountBalance=account.getAccountBalance();
        Transaction transaction =getTransaction(account,account,Amount);
        if(TransactionType.CREDIT.equals(transactionType)){
            transaction.setTransactionType(TransactionType.CREDIT);
            updatedAccountBalance=currentAccountBalance+Amount;
            logger.info("Current Account Balance is "+currentAccountBalance);
            logger.info("Updated Account Balance: "+updatedAccountBalance);
        }
        else{
            transaction.setTransactionType(TransactionType.WITHDRAW);
            try{
                if(currentAccountBalance<Amount){

                    ExceptionsCenter.insufficientBalance();
                }
                else{
                    updatedAccountBalance=currentAccountBalance-Amount;

                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                account.addTransaction(transaction);
                transaction.setStatus(TransactionStatus.FAILED);

                return;
            }
        }
        account.setAccountBalance(updatedAccountBalance);
        account.addTransaction(transaction);
        transaction.setStatus(TransactionStatus.COMPLETED);

    }

    @Override
    public void transferTransaction(Account sender, Account receiver, Double Amount,TransactionStatus transactionStatus)  {

        Transaction transaction=getTransaction(sender,receiver,Amount);
        //* critical section starts here
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(Amount);
        Double currentAccountBalanceSender=sender.getAccountBalance();
        Double currentAccountBalanceReceiver=receiver.getAccountBalance();
        try{
            if(TransactionStatus.FAILED.equals(transactionStatus)){
                ExceptionsCenter.general("Request has to cancel, failed transactio");
            }
            if(currentAccountBalanceSender<Amount){

                ExceptionsCenter.insufficientBalance();
            }
            else{

                currentAccountBalanceSender=currentAccountBalanceSender-Amount;
                sender.setAccountBalance(currentAccountBalanceSender);
                currentAccountBalanceReceiver=currentAccountBalanceReceiver+Amount;
                receiver.setAccountBalance(currentAccountBalanceReceiver);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //if there is insufficient balance means sender has failed transaction
            sender.addTransaction(transaction);
            receiver.addTransaction(transaction);
            transaction.setStatus(TransactionStatus.FAILED);

            return;
        }
        //critical section ends here
        sender.addTransaction(transaction);
        receiver.addTransaction(transaction);
        transaction.setStatus(TransactionStatus.COMPLETED);

    }

    @Override
    public RequestTransaction RequestTransaction(Account sender, Account receiver, Double Amount) {
        Transaction transaction=getTransaction(sender, receiver, Amount);
        RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setTransaction(transaction);
        requestTransaction.setRequestTransactionId(Utility.generateUniqueLongId());
        requestTransaction.setRequestStatus(RequestStatus.PENDING);
        return requestTransaction;
    }



}
