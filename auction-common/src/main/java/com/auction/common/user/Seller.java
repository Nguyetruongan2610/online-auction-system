package com.auction.common.user;

public class Seller extends User {
    private double rating; // Điểm uy tín của người bán

    public Seller(String id, String name, String email, double rating) {
        super(id, name, email);
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String getRole() {
        return "SELLER";
    }
}