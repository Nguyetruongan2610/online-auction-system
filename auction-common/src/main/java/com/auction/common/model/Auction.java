package com.auction.common.model;

import com.auction.common.item.Item;
import com.auction.common.user.Seller;
import java.io.Serializable;

public class Auction implements Serializable {
    private String auctionId;
    private Item item;
    private Seller seller;
    private double startingPrice;
    private Bid highestBid;
    private AuctionStatus status;

    public Auction(String auctionId, Item item, Seller seller, double startingPrice) {
        this.auctionId = auctionId;
        this.item = item;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.status = AuctionStatus.OPEN;
    }

    public String getAuctionId() { return auctionId; }
    public void setAuctionId(String auctionId) { this.auctionId = auctionId; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }
    public double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(double startingPrice) { this.startingPrice = startingPrice; }
    public Bid getHighestBid() { return highestBid; }
    public void setHighestBid(Bid highestBid) { this.highestBid = highestBid; }
    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }

    public double getCurrentPrice() {
        return highestBid != null ? highestBid.getAmount() : startingPrice;
    }
}