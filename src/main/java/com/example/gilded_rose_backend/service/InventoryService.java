package com.example.gilded_rose_backend.service;

import com.example.gilded_rose_backend.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    public Item updateItem(Item item) {

        if (item.name().equals("Sulfuras")) {
            return item;
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
        return items.stream().map(item -> updateItem(item))
                .toList();
    }
}
