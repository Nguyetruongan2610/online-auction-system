package com.auction.server;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionManager {

    public enum AuctionStatus {
        PENDING,
        RUNNING,
        FINISHED
    }

    public static class AuctionSession {
        private final String auctionId;
        private final String itemName;
        private double currentPrice;
        private String highestBidderId;
        private AuctionStatus status;
        private final long endTimeMillis;

        // Khóa riêng cho từng phiên: chống Race Condition nhưng không làm nghẽn phiên khác
        private final ReentrantLock lock = new ReentrantLock(true);

        public AuctionSession(String auctionId, String itemName, double startingPrice, long durationMillis) {
            this.auctionId = auctionId;
            this.itemName = itemName;
            this.currentPrice = startingPrice;
            this.highestBidderId = null;
            this.status = AuctionStatus.RUNNING;
            this.endTimeMillis = System.currentTimeMillis() + durationMillis;
        }

        public ReentrantLock getLock() { return lock; }
        public String getAuctionId() { return auctionId; }
        public String getItemName() { return itemName; }
        public double getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
        public String getHighestBidderId() { return highestBidderId; }
        public void setHighestBidderId(String highestBidderId) { this.highestBidderId = highestBidderId; }
        public AuctionStatus getStatus() { return status; }
        public void setStatus(AuctionStatus status) { this.status = status; }
        public long getEndTimeMillis() { return endTimeMillis; }
    }

    public static class BidResult {
        private final boolean success;
        private final String message;
        private final double currentPrice;
        private final String highestBidderId;

        public BidResult(boolean success, String message, double currentPrice, String highestBidderId) {
            this.success = success;
            this.message = message;
            this.currentPrice = currentPrice;
            this.highestBidderId = highestBidderId;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public double getCurrentPrice() { return currentPrice; }
        public String getHighestBidderId() { return highestBidderId; }
    }

    public interface AuctionEventListener {
        void onBidAccepted(String auctionId, String bidderId, double newPrice);
        void onAuctionFinished(String auctionId, String winnerId, double finalPrice);
    }

    private final ConcurrentHashMap<String, AuctionSession> sessions = new ConcurrentHashMap<>();
    private AuctionEventListener eventListener;

    public void setEventListener(AuctionEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public AuctionSession createSession(String auctionId, String itemName, double startingPrice,
                                        long durationMillis, AuctionScheduler scheduler) {
        AuctionSession session = new AuctionSession(auctionId, itemName, startingPrice, durationMillis);
        if (sessions.putIfAbsent(auctionId, session) != null) {
            throw new IllegalArgumentException("Phiên đấu giá đã tồn tại: " + auctionId);
        }
        if (scheduler != null) {
            scheduler.scheduleClose(session, this);
        }
        return session;
    }

    public BidResult placeBid(String auctionId, String bidderId, double bidAmount) {
        AuctionSession session = sessions.get(auctionId);
        if (session == null) {
            return new BidResult(false, "Không tìm thấy phiên đấu giá.", 0, null);
        }

        BidResult result;
        boolean accepted = false;

        session.getLock().lock();
        try {
            long now = System.currentTimeMillis();

            if (session.getStatus() != AuctionStatus.RUNNING || now >= session.getEndTimeMillis()) {
                return new BidResult(
                        false,
                        "Phiên đấu giá đã kết thúc.",
                        session.getCurrentPrice(),
                        session.getHighestBidderId()
                );
            }

            if (bidAmount <= session.getCurrentPrice()) {
                return new BidResult(
                        false,
                        "Giá đặt (" + bidAmount + ") phải cao hơn giá hiện tại (" + session.getCurrentPrice() + ").",
                        session.getCurrentPrice(),
                        session.getHighestBidderId()
                );
            }

            session.setCurrentPrice(bidAmount);
            session.setHighestBidderId(bidderId);
            accepted = true;
            result = new BidResult(true, "Đặt giá thành công!", bidAmount, bidderId);
        } finally {
            session.getLock().unlock();
        }

        if (accepted && eventListener != null) {
            eventListener.onBidAccepted(auctionId, bidderId, bidAmount);
        }

        return result;
    }

    public void closeAuction(String auctionId) {
        AuctionSession session = sessions.get(auctionId);
        if (session == null) return;

        boolean transitioned = false;
        String winnerId = null;
        double finalPrice = 0;

        session.getLock().lock();
        try {
            if (session.getStatus() == AuctionStatus.RUNNING) {
                session.setStatus(AuctionStatus.FINISHED);
                transitioned = true;
                winnerId = session.getHighestBidderId();
                finalPrice = session.getCurrentPrice();
            }
        } finally {
            session.getLock().unlock();
        }

        if (transitioned) {
            System.out.printf("[AuctionManager] Phiên %s đóng (RUNNING -> FINISHED) | Người thắng: %s | Giá chốt: %.2f%n",
                    auctionId, winnerId != null ? winnerId : "Không có", finalPrice);
            if (eventListener != null) {
                eventListener.onAuctionFinished(auctionId, winnerId, finalPrice);
            }
        }
    }

    public AuctionSession getSession(String auctionId) {
        return sessions.get(auctionId);
    }

    public Collection<AuctionSession> getAllSessions() {
        return Collections.unmodifiableCollection(sessions.values());
    }
}