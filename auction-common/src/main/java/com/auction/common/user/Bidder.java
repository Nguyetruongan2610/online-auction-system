package com.auction.common.user;

public class Bidder extends User {
    private double balance; // Số dư ví của người tham gia đấu giá

    public Bidder(String id, String name, String email, double balance) {
        super(id, name, email);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String getRole() {
        return "BIDDER";
    }
}