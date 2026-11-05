package com.auction.common.item;

public class Electronics extends Item {
    public Electronics(String id, String name, double basePrice) {
        super(id, name, basePrice);
    }

    @Override
    public ItemType getType() {
        return ItemType.ELECTRONICS;
    }
}
