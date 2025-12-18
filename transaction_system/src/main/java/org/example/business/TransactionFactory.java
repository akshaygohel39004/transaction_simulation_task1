package org.example.business;

import org.example.model.PaymentService;

public class TransactionFactory {
    public static TransactionProcessor getTransactionPaymentService(PaymentService paymentService){
        return switch (paymentService) {
            case PaymentService.MobileGateway -> new TransactionProcessorThroughMobileGateWay();
            case PaymentService.CardProcessor -> new TransactionProcessorThroughCardProcessor();
            case PaymentService.UPI -> new TransactionProcessorThroughUPI();
            default -> throw new RuntimeException("Unsupported PaymentService");
        };
    }
}
