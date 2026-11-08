package com.auction.server;

import com.auction.common.model.Auction;
import com.auction.common.model.AuctionStatus;
import com.auction.common.model.Bid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionManager {
    // 1. Biến static duy nhất lưu instance của Singleton
    private static AuctionManager instance;

    // Danh sách các phiên đấu giá được lưu trữ theo auctionId
    private final Map<String, Auction> auctions;

    // 2. Constructor private để ngăn việc new AuctionManager() từ bên ngoài
    private AuctionManager() {
        auctions = new HashMap<>();
    }

    // 3. Phương thức static toàn cục để lấy instance duy nhất (Thread-safe)
    public static synchronized AuctionManager getInstance() {
        if (instance == null) {
            instance = new AuctionManager();
        }
        return instance;
    }

    // --- CÁC PHƯƠNG THỨC QUẢN LÝ PHIÊN ĐẤU GIÁ ---

    /**
     * Thêm một phiên đấu giá mới vào hệ thống
     */
    public synchronized void addAuction(Auction auction) {
        if (auction != null) {
            auctions.put(auction.getAuctionId(), auction);
            System.out.println("Đã thêm phiên đấu giá: " + auction.getAuctionId());
        }
    }

    /**
     * Tìm một phiên đấu giá theo ID
     */
    public synchronized Auction getAuction(String auctionId) {
        return auctions.get(auctionId);
    }

    /**
     * Lấy toàn bộ danh sách phiên đấu giá (dùng để gửi về cho Client hiển thị)
     */
    public synchronized List<Auction> getAllAuctions() {
        return new ArrayList<>(auctions.values());
    }

    /**
     * Kích hoạt phiên đấu giá (chuyển trạng thái từ OPEN -> RUNNING)
     */
    public synchronized boolean startAuction(String auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            System.out.println("Không tìm thấy phiên đấu giá: " + auctionId);
            return false;
        }
        if (auction.getStatus() != AuctionStatus.OPEN) {
            System.out.println("Phiên không ở trạng thái OPEN, không thể bắt đầu!");
            return false;
        }
        auction.setStatus(AuctionStatus.RUNNING);
        System.out.println("Phiên đấu giá " + auctionId + " đã chính thức BẮT ĐẦU (RUNNING)!");
        return true;
    }

    /**
     * Kết thúc phiên đấu giá (chuyển sang FINISHED)
     */
    public synchronized boolean endAuction(String auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction != null && auction.getStatus() == AuctionStatus.RUNNING) {
            auction.setStatus(AuctionStatus.FINISHED);
            System.out.println("Phiên đấu giá " + auctionId + " đã KẾT THÚC!");
            return true;
        }
        return false;
    }

    /**
     * Xử lý đặt giá (Thread-safe bằng synchronized để chống Race Condition)
     */
    public synchronized boolean placeBid(String auctionId, Bid newBid) {
        Auction auction = auctions.get(auctionId);

        // 1. Kiểm tra phiên tồn tại
        if (auction == null) {
            System.out.println("Lỗi: Phiên đấu giá không tồn tại!");
            return false;
        }

        // 2. Kiểm tra phiên có đang diễn ra hay không
        if (auction.getStatus() != AuctionStatus.RUNNING) {
            System.out.println("Lỗi: Phiên đấu giá hiện không mở để đặt giá!");
            return false;
        }

        // 3. Kiểm tra số dư ví của người đặt giá
        if (newBid.getBidder().getBalance() < newBid.getAmount()) {
            System.out.println("Lỗi: Số dư tài khoản không đủ để đặt mức giá này!");
            return false;
        }

        // 4. Kiểm tra giá đặt phải cao hơn giá hiện tại
        double currentPrice = auction.getCurrentPrice();
        if (newBid.getAmount() <= currentPrice) {
            System.out.println(
                    "Lỗi: Giá đặt (" + newBid.getAmount() + ") phải cao hơn giá hiện tại (" + currentPrice + ")!");
            return false;
        }

        // 5. Cập nhật lượt đặt giá cao nhất thành công
        auction.setHighestBid(newBid);
        System.out.println(">> Đặt giá thành công! Phiên: " + auctionId
                + " | Người mua: " + newBid.getBidder().getName()
                + " | Giá mới: " + newBid.getAmount());
        return true;
    }
}