package org.example.business;

import org.example.model.*;
import org.example.stats.PaymentStatsRouter;

public class MakeTransactionThroughMobileGateWay implements IMakeTransactions {
    private final PaymentStatsRouter statsRouter;
    private final TransactionHelper transactionHelper;
    public MakeTransactionThroughMobileGateWay(PaymentStatsRouter statsRouter) {
        this.statsRouter = statsRouter;
        this.transactionHelper=new TransactionHelper(statsRouter,PaymentService.MobileGateway);
    }

    //for deposit/withdraw into account


    @Override
    public void SelfTransaction(Account account, Double Amount, boolean isDeposit) {
        transactionHelper.doSelfTransaction(account,Amount,isDeposit);
    }
    @Override
    public void transferTransaction(Account sender, Account receiver, Double Amount,boolean willCancel)  {
        transactionHelper.doTransferTransaction(sender,receiver,Amount,willCancel);
    }

    @Override
    public RequestTransaction RequestTransaction(Account sender, Account receiver, Double Amount) {
        return transactionHelper.doRequestTransaction(sender,receiver,Amount);
    }



}
