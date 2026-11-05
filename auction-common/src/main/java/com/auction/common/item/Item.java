package com.auction.common.item;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private String id;
    private String name;
    private double basePrice;

    public Item(String id, String name, double basePrice) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }

    public abstract ItemType getType();
}
