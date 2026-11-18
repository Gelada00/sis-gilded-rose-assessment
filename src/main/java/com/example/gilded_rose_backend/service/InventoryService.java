package com.example.gilded_rose_backend.service;

import com.example.gilded_rose_backend.exception.InvalidInventoryException;
import com.example.gilded_rose_backend.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public Item updateItem(Item item) {
        if (item == null) {
            throw new InvalidInventoryException("Item must not be null");
        }
        if (item.name() == null || item.name().isBlank()) {
            throw new InvalidInventoryException("Item name must not be blank");
        }

        if (item.quality() < 0) {
            throw new InvalidInventoryException("Item quality must not be negative");
        }

        if (item.name().equals("Sulfuras")) {
            return new Item(item.name(), item.sellIn(), 80);
        }

        boolean expired = item.sellIn() <= 0;

        int newQuality = switch (item.name()) {

            case "Aged Brie" -> item.quality() + (expired ? 2 : 1);

            case "Conjured" -> item.quality() - (expired ? 4 : 2);

            case "Backstage passes" -> calculateBackstageQuality(item);

            default -> item.quality() - (expired ? 2 : 1);
        };

        newQuality = Math.max(0, Math.min(50, newQuality));

        return new Item(
                item.name(),
                item.sellIn() - 1,
                newQuality
        );
    }

    private int calculateBackstageQuality(Item item) {

        int sellIn = item.sellIn();

        if (sellIn <= 0) {
            return 0;
        }
        if (sellIn <= 5) {
            return item.quality() + 3;
        }
        if (sellIn <= 10) {
            return item.quality() + 2;
        }

        return item.quality() + 1;
    }

    public List<Item> updateInventory(List<Item> items) {
        if (items == null) {
            throw new InvalidInventoryException("Inventory must not be null");
        }
        log.info("Starting inventory update for {} items", items.size());

        List<Item> updatedItems =  items.stream().map(item -> updateItem(item))
                .toList();

        log.info("Inventory updated successfully");

        return updatedItems;
    }
}
