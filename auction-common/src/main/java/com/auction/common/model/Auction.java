package com.auction.common.model;

import com.auction.common.item.Item;
import com.auction.common.user.Seller;

public class Auction {
    private String auctionId;
    private Item item; // Món hàng được đấu giá
    private Seller seller; // Người bán món hàng
    private double startingPrice; // Giá khởi điểm
    private Bid highestBid; // Lượt đặt giá cao nhất hiện tại
    private AuctionStatus status; // Trạng thái phiên (OPEN, RUNNING, ...)

    // Constructor đã được bổ sung tham số Item item
    public Auction(String auctionId, Item item, Seller seller, double startingPrice) {
        this.auctionId = auctionId;
        this.item = item;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.status = AuctionStatus.OPEN; // Mặc định khi tạo mới là OPEN
    }

    // --- GETTERS & SETTERS ---
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Bid getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Bid highestBid) {
        this.highestBid = highestBid;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    /**
     * Phương thức tiện ích lấy giá hiện tại của phiên đấu giá:
     * - Nếu đã có người ra giá (highestBid != null) -> trả về giá đó.
     * - Nếu chưa có ai ra giá -> trả về giá khởi điểm (startingPrice).
     */
    public double getCurrentPrice() {
        return highestBid != null ? highestBid.getAmount() : startingPrice;
    }
}