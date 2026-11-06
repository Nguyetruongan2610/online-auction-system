package com.auction.common.model;
import com.auction.common.user.Seller;

public class Auction {
    private String auctionId;
    private Item item;
    private Seller seller;
    private double startingPrice;
    private Bid highestBid;
    private AuctionStatus status;

    public Auction(String auctionId, Seller seller, double startingPrice) {
        this.auctionId = auctionId;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.status = AuctionStatus.OPEN; // Mặc định khi tạo mới là OPEN
    }

    public String getAuctionId() { return auctionId; }
    public double getStartingPrice() { return startingPrice; }

    public Bid getHighestBid() { return highestBid; }
    public void setHighestBid(Bid highestBid) { this.highestBid = highestBid; }

    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }
}
