package org.example.business;

import org.example.model.PaymentService;

public class TransactionFactory {
    public static IMakeTransactions getTransactionPaymentService(PaymentService paymentService){
        switch (paymentService){
            case PaymentService.MobileGateway: return new MakeTransactionThroughMobileGateWay();
            case PaymentService.CardProcessor: return new MakeTransactionThroughCardProcessor();
            case PaymentService.UPI: return new MakeTransactionThroughUPI();
        }
        return null;
    }
}
