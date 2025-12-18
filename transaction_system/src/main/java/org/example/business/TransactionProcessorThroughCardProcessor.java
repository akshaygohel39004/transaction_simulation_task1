package org.example.business;

import org.example.exceptions.ExceptionsCenter;
import org.example.model.*;
import org.example.stats.PaymentStatsRouter;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import org.slf4j.Logger;


public class TransactionProcessorThroughCardProcessor implements TransactionProcessor {
    private final PaymentStatsRouter statsRouter;
    private final TransactionHelper transactionHelper;
    public TransactionProcessorThroughCardProcessor(PaymentStatsRouter statsRouter) {
        this.statsRouter = statsRouter;
        this.transactionHelper =new TransactionHelper(statsRouter,PaymentService.CardProcessor);
    }



    @Override
    public void SelfTransaction(Account account, Double Amount, TransactionType transactionType) {
        transactionHelper.doSelfTransaction(account,Amount,transactionType);
    }


    @Override
    public void transferTransaction(Account sender, Account receiver, Double Amount,TransactionStatus transactionStatus)  {
        transactionHelper.doTransferTransaction(sender,receiver,Amount,transactionStatus);
    }
    @Override
    public RequestTransaction RequestTransaction(Account sender, Account receiver, Double Amount) {
        return transactionHelper.doRequestTransaction(sender,receiver,Amount);
    }



}
