package com.auction.server;

import com.auction.common.model.Auction;
import com.auction.common.model.Bid;
import com.auction.common.model.AuctionStatus;
import java.util.HashMap;
import java.util.Map;

public class AuctionManager {
    private static AuctionManager instance;

    private Map<String, Auction> auctions;

    private AuctionManager() {
        auctions = new HashMap<>();
    }

    public static synchronized AuctionManager getInstance() {
        if (instance == null) {
            instance = new AuctionManager();
        }
        return instance;
    }

    public void addAuction(Auction auction) {
        auctions.put(auction.getAuctionId(), auction);
    }

    public synchronized boolean placeBid(String auctionId, Bid newBid) {
        Auction auction = auctions.get(auctionId);

        if (auction == null) {
            System.out.println("Phiên đấu giá không tồn tại!");
            return false;
        }

        if (auction.getStatus() != AuctionStatus.RUNNING) {
            System.out.println("Phiên đấu giá không mở!");
            return false;
        }

        double currentHighestPrice = auction.getHighestBid() != null ?
                auction.getHighestBid().getAmount() :
                auction.getStartingPrice();

        if (newBid.getAmount() <= currentHighestPrice) {
            System.out.println("Giá đưa ra phải cao hơn giá hiện tại!");
            return false;
        }

        auction.setHighestBid(newBid);
        System.out.println("Đặt giá thành công! Người mua: " + newBid.getBidder().getName() + " - Giá: " + newBid.getAmount());
        return true;
    }
}