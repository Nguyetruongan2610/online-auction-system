package com.auction.common.model;

public enum AuctionStatus {
    OPEN,       // Mở cửa chờ bắt đầu
    RUNNING,    // Đang diễn ra
    FINISHED,   // Đã kết thúc (chờ thanh toán)
    PAID,       // Đã thanh toán
    CANCELED    // Bị hủy
}