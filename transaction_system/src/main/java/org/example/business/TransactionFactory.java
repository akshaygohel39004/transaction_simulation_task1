package org.example.business;

import org.example.model.PaymentService;
import org.example.stats.PaymentStatsRouter;

public class TransactionFactory {
    public static IMakeTransactions getTransactionPaymentService(PaymentService paymentService, PaymentStatsRouter statsRouter){
        switch (paymentService){
            case PaymentService.MobileGateway: return new MakeTransactionThroughMobileGateWay(statsRouter);
            case PaymentService.CardProcessor: return new MakeTransactionThroughCardProcessor(statsRouter);
            case PaymentService.UPI: return new MakeTransactionThroughUPI(statsRouter);
        }
        return null;
    }
}
