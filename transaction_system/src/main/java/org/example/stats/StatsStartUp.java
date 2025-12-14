package org.example.stats;

import org.example.model.PaymentService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatsStartUp {
    private PaymentStatsRegistry mobileRegistry = new PaymentStatsRegistry();
    private PaymentStatsRegistry cardRegistry   = new PaymentStatsRegistry();
    private PaymentStatsRegistry upiRegistry    = new PaymentStatsRegistry();
    private ScheduledExecutorService statsScheduler = Executors.newScheduledThreadPool(3);

    public PaymentStatsRouter start(){
        PaymentStatsRouter statsRouter =
                new PaymentStatsRouter(
                        mobileRegistry,
                        cardRegistry,
                        upiRegistry
                );


        statsScheduler.scheduleAtFixedRate(
                new PaymentStatsWorker(
                        PaymentService.MobileGateway,
                        mobileRegistry),
                0, 1, TimeUnit.SECONDS);

        statsScheduler.scheduleAtFixedRate(
                new PaymentStatsWorker(
                        PaymentService.CardProcessor,
                        cardRegistry),
                0, 1, TimeUnit.SECONDS);

        statsScheduler.scheduleAtFixedRate(
                new PaymentStatsWorker(
                        PaymentService.UPI,
                        upiRegistry),
                0, 1, TimeUnit.SECONDS);

        return statsRouter;
    }

    public void shutDown(){
        statsScheduler.shutdown();
        try {
            statsScheduler.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }

}
