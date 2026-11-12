package com.auction.server.demo;

import com.auction.server.AuctionManager;
import com.auction.server.AuctionScheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionLockDemo {
    public static void main(String[] args) throws InterruptedException {
        AuctionManager manager = new AuctionManager();
        AuctionScheduler scheduler = new AuctionScheduler(4);

        manager.createSession("AUC-01", "Laptop Gaming", 1000.0, 2000, scheduler);
        manager.createSession("AUC-02", "Bàn phím cơ", 200.0, 2000, scheduler);

        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneGate = new CountDownLatch(threads);

        for (int i = 1; i <= threads; i++) {
            final String user = "User-" + i;
            final double price = 1000.0 + i;
            pool.submit(() -> {
                try {
                    startGate.await();
                    manager.placeBid("AUC-01", user, price);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneGate.countDown();
                }
            });
        }

        startGate.countDown();
        doneGate.await();

        Thread.sleep(2500);

        System.out.println("Trạng thái AUC-01: " + manager.getSession("AUC-01").getStatus());
        System.out.println("Giá chốt AUC-01: " + manager.getSession("AUC-01").getCurrentPrice());
        System.out.println("Người thắng AUC-01: " + manager.getSession("AUC-01").getHighestBidderId());

        AuctionManager.BidResult lateBid = manager.placeBid("AUC-01", "LateUser", 9999.0);
        System.out.println("Đặt giá sau khi đóng phiên: " + lateBid.isSuccess() + " -> " + lateBid.getMessage());

        pool.shutdown();
        scheduler.shutdown();
    }
}
