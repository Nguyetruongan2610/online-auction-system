package com.auction.server;

import com.auction.server.AuctionManager.AuctionSession;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AuctionScheduler {

    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public AuctionScheduler(int threadPoolSize) {
        AtomicInteger threadCount = new AtomicInteger(1);
        this.scheduler = Executors.newScheduledThreadPool(threadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("auction-scheduler-" + threadCount.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });
    }

    public void scheduleClose(AuctionSession session, AuctionManager manager) {
        String auctionId = session.getAuctionId();
        long delayMillis = Math.max(0, session.getEndTimeMillis() - System.currentTimeMillis());

        ScheduledFuture<?> future = scheduler.schedule(() -> {
            try {
                scheduledTasks.remove(auctionId);
                manager.closeAuction(auctionId);
            } catch (Exception e) {
                System.err.println("[AuctionScheduler] Lỗi đóng phiên " + auctionId + ": " + e.getMessage());
            }
        }, delayMillis, TimeUnit.MILLISECONDS);

        ScheduledFuture<?> oldTask = scheduledTasks.put(auctionId, future);
        if (oldTask != null && !oldTask.isDone()) {
            oldTask.cancel(false);
        }
    }

    public void cancelSchedule(String auctionId) {
        ScheduledFuture<?> future = scheduledTasks.remove(auctionId);
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}