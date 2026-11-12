package com.auction.common.model;
import com.auction.common.item.Item;
import com.auction.common.user.Seller;
import java.util.ArrayList;
import java.util.List;

public class Auction {
    private String auctionId;
    private Item item;
    private Seller seller;
    private double startingPrice;
    private Bid highestBid;
    private AuctionStatus status;
    private List<Bid> bidHistory = new ArrayList<>();

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

    public Seller getSeller() { return seller; }

    public List<Bid> getBidHistory() { return bidHistory; }

    public double getCurrentPrice() {
        if (highestBid == null) return startingPrice;
        return highestBid.getAmount();
    }

    public void addBid(Bid bid) {
        bidHistory.add(bid);
        this.highestBid = bid;
    }

    public void start() {
        if (status != AuctionStatus.OPEN)
            throw new IllegalStateException("Chi phien OPEN moi start duoc");
        status = AuctionStatus.RUNNING;
    }

    public void finish() {
        if (status != AuctionStatus.RUNNING)
            throw new IllegalStateException("Chi phien RUNNING moi finish duoc");
        status = AuctionStatus.FINISHED;
    }

    public void pay() {
        if (status != AuctionStatus.FINISHED)
            throw new IllegalStateException("Chi phien FINISHED moi thanh toan duoc");
        status = AuctionStatus.PAID;
    }

    public void cancel() {
        if (status != AuctionStatus.OPEN && status != AuctionStatus.RUNNING)
            throw new IllegalStateException("Chi huy duoc phien OPEN hoac RUNNING");
        status = AuctionStatus.CANCELED;
    }
}
