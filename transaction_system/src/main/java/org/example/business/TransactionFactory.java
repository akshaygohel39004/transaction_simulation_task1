package org.example.business;

import org.example.model.PaymentService;
import org.example.stats.PaymentStatsRouter;

public class TransactionFactory {
    public static TransactionProcessor getTransactionPaymentService(PaymentService paymentService, PaymentStatsRouter  paymentStatsRouter) {
        return switch (paymentService) {
            case PaymentService.MobileGateway -> new TransactionProcessorThroughMobileGateWay(paymentStatsRouter);
            case PaymentService.CardProcessor -> new TransactionProcessorThroughCardProcessor(paymentStatsRouter);
            case PaymentService.UPI -> new TransactionProcessorThroughUPI(paymentStatsRouter);
            default -> throw new RuntimeException("Unsupported PaymentService");
        };
    }
}
