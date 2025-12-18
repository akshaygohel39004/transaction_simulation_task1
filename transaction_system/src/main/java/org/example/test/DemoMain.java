package org.example.test;

import org.example.test.DemoClient;
import org.example.stats.*;

public class DemoMain {

    public static void main(String[] args) {

        StatsStartUp statsStartUp = new StatsStartUp();
        PaymentStatsRouter paymentStatsRouter=statsStartUp.start();
        DemoClient demoClient=new DemoClient(paymentStatsRouter);
        demoClient.start();
        statsStartUp.shutDown();

    }
}
