package org.example.business;

import org.example.exceptions.ThrowExcpetions;
import org.example.model.*;


import java.time.LocalDateTime;

public class MakeTransactionThroughCardProcessor implements IMakeTransactions {


    public MakeTransactionThroughCardProcessor() {

    }

    //for deposit/withdraw into account
    private Transaction getTransaction(Account sender,Account receiver,Double Amount){
        Transaction transaction = new Transaction();
        transaction.setTransactionInitiationTime(LocalDateTime.now());
        transaction.setPaymentService(PaymentService.CardProcessor);
        transaction.setTransactionId(Utility.generateUniqueLongId());
        transaction.setStatus(TransactionStatus.INITIATION);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(Amount);
        return transaction;
    }

    @Override
    public void SelfTransaction(Account account, Double Amount, boolean isDeposit) {
        Double updatedAccountBalance,currentAccountBalance;
        currentAccountBalance=updatedAccountBalance=account.getAccountBalance();
        Transaction transaction =getTransaction(account,account,Amount);
        if(isDeposit){
            transaction.setTransactionType(TransactionType.CREDIT);
            updatedAccountBalance=currentAccountBalance+Amount;
            System.out.println("Current Account Balance is "+currentAccountBalance);
            System.out.println("Updated Account Balance is "+updatedAccountBalance);
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
    public void transferTransaction(Account sender, Account receiver, Double Amount,boolean willCancel)  {

        Transaction transaction=getTransaction(sender,receiver,Amount);
        //* critical section starts here
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(Amount);
        Double currentAccountBalanceSender=sender.getAccountBalance();
        Double currentAccountBalanceReceiver=receiver.getAccountBalance();
        try{
            if(willCancel){
                ThrowExcpetions.general("Request has to cancel, failed transactio");
            }
            if(currentAccountBalanceSender<Amount){

                ThrowExcpetions.general("Insufficient Balance");
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
