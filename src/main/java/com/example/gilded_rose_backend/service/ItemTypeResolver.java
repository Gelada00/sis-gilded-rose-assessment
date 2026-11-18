package com.example.gilded_rose_backend.service;

import java.util.Locale;

public class ItemTypeResolver {

    public static ItemType resolve(String name) {
        String normalised = name.trim().toLowerCase(Locale.ROOT);

        return switch (normalised) {
            case "aged brie" -> ItemType.AGED_BRIE;
            case "backstage passes" -> ItemType.BACKSTAGE_PASSES;
            case "sulfuras" -> ItemType.SULFURAS;
            default -> normalised.equals("conjured")
                    || normalised.startsWith("conjured")
                    ? ItemType.CONJURED
                    : ItemType.NORMAL;
        };
    }
}
