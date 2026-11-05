package com.auction.common.item;

public class Art extends Item {
    public Art(String id, String name, double basePrice) {
        super(id, name, basePrice);
    }

    @Override
    public ItemType getType() {
        return ItemType.ART;
    }
}
