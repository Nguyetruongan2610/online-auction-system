package com.auction.common.item;

public class ItemFactory {
    public static Item createItem(ItemType type, String id, String name, double basePrice) {
        if (type == null) {
            throw new IllegalArgumentException("ItemType cannot be null");
        }
        return switch (type) {
            case ELECTRONICS -> new Electronics(id, name, basePrice);
            case ART -> new Art(id, name, basePrice);
            case VEHICLE -> new Vehicle(id, name, basePrice);
        };
    }
}