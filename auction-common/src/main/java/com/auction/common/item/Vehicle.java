package com.auction.common.item;

public class Vehicle extends Item {
    public Vehicle(String id, String name, double basePrice) {
        super(id, name, basePrice);
    }

    @Override
    public ItemType getType() {
        return ItemType.VEHICLE;
    }
}
