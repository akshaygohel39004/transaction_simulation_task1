package org.example.stats;


import org.example.model.Transaction;
import java.util.LinkedList;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PaymentStatsRegistry {


    private final Queue<Transaction> queue = new ConcurrentLinkedQueue<>();

    // Call by transaction threads
    public void submit(Transaction transaction) {
        queue.offer(transaction);
    }

    // stats worker will Call once per second
    public Queue<Transaction> drain() {
        Queue<Transaction> batch = new LinkedList<>();
        Transaction tx;
        while ((tx = queue.poll()) != null) {
            batch.add(tx);
        }
        return batch;
    }
}
