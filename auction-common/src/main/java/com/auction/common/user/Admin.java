package com.auction.common.user;

public class Admin extends User {
    public Admin(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}