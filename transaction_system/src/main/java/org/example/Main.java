package org.example;
import org.example.client.Client;
import org.example.stats.*;

public class Main {

    public static void main(String[] args) {

        StatsStartUp statsStartUp = new StatsStartUp();
        PaymentStatsRouter paymentStatsRouter=statsStartUp.start();
        Client client = new Client(paymentStatsRouter);
        client.start();
        statsStartUp.shutDown();

    }
}
