package org.example.business;

import org.example.model.PaymentService;

public class TransactionFactory {
    public static TransactionProcessor getTransactionPaymentService(PaymentService paymentService){
        switch (paymentService){
            case PaymentService.MobileGateway: return new TransactionProcessorThroughMobileGateWay();
            case PaymentService.CardProcessor: return new TransactionProcessorThroughCardProcessor();
            case PaymentService.UPI: return new TransactionProcessorThroughUPI();
        }
        return null;
    }
}
