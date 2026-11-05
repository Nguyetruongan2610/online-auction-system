package com.auction.common.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemFactoryTest {

    @Test
    void testCreateElectronics() {
        Item item = ItemFactory.createItem(ItemType.ELECTRONICS, "E01", "MacBook Pro", 2000.0);
        assertNotNull(item);
        assertInstanceOf(Electronics.class, item);
        assertEquals("E01", item.getId());
        assertEquals("MacBook Pro", item.getName());
        assertEquals(2000.0, item.getBasePrice());
        assertEquals(ItemType.ELECTRONICS, item.getType());
    }

    @Test
    void testCreateArtAndVehicle() {
        Item art = ItemFactory.createItem(ItemType.ART, "A01", "Starry Night", 5000.0);
        Item vehicle = ItemFactory.createItem(ItemType.VEHICLE, "V01", "Honda Civic", 15000.0);

        assertEquals(ItemType.ART, art.getType());
        assertEquals(ItemType.VEHICLE, vehicle.getType());
    }

    @Test
    void testCreateWithNullTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                ItemFactory.createItem(null, "X01", "Unknown", 100.0)
        );
    }
}
