package com.auction.common.user;

public class Admin extends User {
    public Admin(String id, String name, String email) {
        super(id, name, email);
    }
    // Có thể thêm các quyền quản trị viên ở đây
}