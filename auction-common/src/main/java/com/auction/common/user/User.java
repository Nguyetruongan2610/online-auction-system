package com.auction.common.user;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Phương thức trừu tượng thể hiện tính ĐA HÌNH (Polymorphism).
     * Mỗi lớp con (Bidder, Seller, Admin) bắt buộc phải tự định nghĩa vai trò của
     * mình.
     */
    public abstract String getRole();
}