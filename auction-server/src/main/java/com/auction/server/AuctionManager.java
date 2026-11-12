package com.auction.server;

import com.auction.common.model.Auction;
import com.auction.common.model.AuctionStatus;
import com.auction.common.model.Bid;
import com.auction.common.user.Seller;

import java.util.HashMap;
import java.util.Map;

public class AuctionManager {

    public static final double MIN_BID_STEP = 10000;

    private Map<String, Auction> auctions = new HashMap<>();

    public Auction createAuction(String auctionId, Seller seller, double startingPrice) {
        Auction auction = new Auction(auctionId, seller, startingPrice);
        auctions.put(auctionId, auction);
        return auction;
    }

    public Auction getAuction(String auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null)
            throw new IllegalArgumentException("Khong tim thay phien: " + auctionId);
        return auction;
    }

    public void startAuction(String auctionId)  { getAuction(auctionId).start(); }
    public void finishAuction(String auctionId) { getAuction(auctionId).finish(); }
    public void payAuction(String auctionId)    { getAuction(auctionId).pay(); }
    public void cancelAuction(String auctionId) { getAuction(auctionId).cancel(); }

    public void placeBid(String auctionId, Bid bid) {
        Auction auction = getAuction(auctionId);
        validateBid(auction, bid);   // kiểm tra hợp lệ
        auction.addBid(bid);         // hợp lệ thì lưu
    }

    private void validateBid(Auction auction, Bid bid) {
        if (auction.getStatus() != AuctionStatus.RUNNING)
            throw new IllegalStateException("Phien chua chay hoac da ket thuc");

        if (bid.getBidder().getId().equals(auction.getSeller().getId()))
            throw new IllegalArgumentException("Seller khong duoc dau gia hang cua minh");

        double minAccepted;
        if (auction.getHighestBid() == null) {
            minAccepted = auction.getStartingPrice();
        } else {
            minAccepted = auction.getCurrentPrice() + MIN_BID_STEP;
        }
        if (bid.getAmount() < minAccepted)
            throw new IllegalArgumentException("Gia dat phai >= " + minAccepted);
    }
}