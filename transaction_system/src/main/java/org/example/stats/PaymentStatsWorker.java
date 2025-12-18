package org.example.stats;

import org.example.model.PaymentService;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Queue;

public class PaymentStatsWorker implements Runnable {

    private final PaymentService paymentService;
    private final PaymentStatsRegistry registry;
    private final String fileName;

    public PaymentStatsWorker(PaymentService paymentService, PaymentStatsRegistry registry) {
        this.paymentService = paymentService;
        this.registry = registry;
        this.fileName = this.paymentService.name().toLowerCase() + "_stats.txt";
    }

    @Override
    public void run() {

        Queue<Transaction> transactions = registry.drain();

        if(transactions.isEmpty()){ return; } //comment if you want every second view
        int total = transactions.size();
        int success = 0;
        int failed = 0;

        try (FileWriter writer = new FileWriter(fileName, true)) {


            for (Transaction tx : transactions) {

                if (tx.getStatus() == TransactionStatus.COMPLETED) {
                    success++;
                } else if (tx.getStatus() == TransactionStatus.FAILED) {
                    failed++;
                }

                writer.write(
                        "TxId: " + tx.getTransactionId() +
                                " | Sender: " + tx.getSender().getAccountNumber() +
                                " | Receiver: " + tx.getReceiver().getAccountNumber() +
                                " | Amount: " + tx.getAmount() +
                                " | Status: " + tx.getStatus() + "\n"
                );
            }
            writer.write(
                    "Time: " + LocalDateTime.now() +
                            "SUMMARY is Total: " + total +
                            " Success: " + success +
                            " Failed: " + failed + "\n\n"
            );


        } catch (Exception e) {
            System.out.println("Exception in PaymentStatsWorker: " + e.getMessage());
        }
    }
}
