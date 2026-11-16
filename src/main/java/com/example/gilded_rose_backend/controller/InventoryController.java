package com.example.gilded_rose_backend.controller;

import com.example.gilded_rose_backend.model.Item;
import com.example.gilded_rose_backend.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/update")
    public List<Item> updateInventory(@RequestBody List<Item> items) {
        return inventoryService.updateInventory(items);
    }
}
