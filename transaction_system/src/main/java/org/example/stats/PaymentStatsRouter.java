package org.example.stats;

import org.example.model.PaymentService;
import org.example.model.Transaction;

import java.util.Map;

public class PaymentStatsRouter {

    private final Map<PaymentService, PaymentStatsRegistry> registryMap;

    public PaymentStatsRouter(PaymentStatsRegistry mobile,
                              PaymentStatsRegistry card,
                              PaymentStatsRegistry upi) {

        registryMap = Map.of(
                PaymentService.MobileGateway, mobile,
                PaymentService.CardProcessor, card,
                PaymentService.UPI, upi
        );
    }

    public void submit(Transaction transaction) {
        registryMap
                .get(transaction.getPaymentService())
                .submit(transaction);
    }
}
