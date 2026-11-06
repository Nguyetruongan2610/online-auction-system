package com.auction.common.user;

public class Bidder extends User {
    private double balance; // Ví tiền của người mua

    public Bidder(String id, String name, String email, double balance) {
        super(id, name, email);
        this.balance = balance;
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}