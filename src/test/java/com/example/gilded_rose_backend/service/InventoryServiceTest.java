package com.example.gilded_rose_backend.service;

import com.example.gilded_rose_backend.exception.InvalidInventoryException;
import com.example.gilded_rose_backend.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventoryServiceTest {
    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService();
    }

    @Test
    public void normalItemShouldDecreaseQualityByOne() {
        Item normalItem = new Item("Normal Item", 5, 10);

        Item result = service.updateItem(normalItem);

        assertEquals(4, result.sellIn());
        assertEquals(9, result.quality());
    }

    @Test
    public void expiredNormalItemsShouldDecreaseQualityByTwo() {
        Item normalItem = new Item("Normal Item", -1, 10);

        Item result = service.updateItem(normalItem);

        assertEquals(-2, result.sellIn());
        assertEquals(8, result.quality());
    }

    @Test
    public void qualityShouldRemainZeroIfItsZeroPriorTheUpdate() {
        Item normalItem = new Item("Normal Item", 3, 0);

        Item result = service.updateItem(normalItem);

        assertEquals(2, result.sellIn());
        assertEquals(0, result.quality());

    }

    @Test
    public void qualityShouldNeverBecomeNegative() {
        Item normalItem = new Item("Normal Item", -1, 1);

        Item result = service.updateItem(normalItem);

        assertEquals(-2, result.sellIn());
        assertEquals(0, result.quality());
    }

    @Test
    public void agedBrieShouldIncreaseQualityByOne() {
        Item agedBrie = new Item("Aged Brie", 5, 10);

        Item result = service.updateItem(agedBrie);

        assertEquals(4, result.sellIn());
        assertEquals(11, result.quality());
    }

    @Test
    public void expiredAgedBrieShouldIncreaseQualityByTwo() {
        Item agedBrie = new Item("Aged Brie", -1, 10);

        Item result = service.updateItem(agedBrie);

        assertEquals(-2, result.sellIn());
        assertEquals(12, result.quality());
    }

    @Test
    public void agedBrieQualityShouldRemainAt50AfterItemUpdated() {
        Item agedBrie = new Item("Aged Brie", -1, 50);

        Item result = service.updateItem(agedBrie);

        assertEquals(-2, result.sellIn());
        assertEquals(50, result.quality());
    }

    @Test
    public void sulfurasShouldNeverChange() {
        Item sulfuras = new Item("Sulfuras", 0, 80);

        Item result = service.updateItem(sulfuras);

        assertEquals(0, result.sellIn());
        assertEquals(80, result.quality());
    }

    @Test
    public void conjuredItemsShouldDecreaseQualityByTwo() {
        Item conjured = new Item("Conjured", 2, 10);

        Item result = service.updateItem(conjured);

        assertEquals(1, result.sellIn());
        assertEquals(8, result.quality());
    }

    @Test
    public void conjuredExpiredItemsShouldDecreaseQualityByFour() {
        Item conjured = new Item("Conjured", -1, 10);

        Item result = service.updateItem(conjured);

        assertEquals(-2, result.sellIn());
        assertEquals(6, result.quality());
    }

    @Test
    public void conjuredItemWithZeroSellInShouldDegradeByFour() {
        Item conjured = new Item("Conjured", 0, 10);

        Item result = service.updateItem(conjured);

        assertEquals(-1, result.sellIn());
        assertEquals(6, result.quality());
    }

    @Test
    public void conjuredQualityShouldNeverBecomeNegative() {
        Item conjured = new Item("Conjured", -1, 2);

        Item result = service.updateItem(conjured);

        assertEquals(-2, result.sellIn());
        assertEquals(0, result.quality());
    }

    @Test
    public void backstagePassesShouldIncreaseQualityByOneWhenMoreThanTenDaysRemain() {
        Item backstagePasses = new Item("Backstage passes", 15, 10);

        Item result = service.updateItem(backstagePasses);

        assertEquals(14, result.sellIn());
        assertEquals(11, result.quality());
    }

    @Test
    public void backstagePassesQualityShouldDropToZeroAfterConcert() {
        Item backstagePasses = new Item("Backstage passes", -1, 20);

        Item result = service.updateItem(backstagePasses);

        assertEquals(-2, result.sellIn());
        assertEquals(0, result.quality());
    }

    @Test
    public void backstagePassesShouldIncreaseQualityByTwoWhenMoreThanFiveDaysRemain() {
        Item backstagePasses = new Item("Backstage passes", 6, 10);

        Item result = service.updateItem(backstagePasses);

        assertEquals(5, result.sellIn());
        assertEquals(12, result.quality());
    }

    @Test
    public void backstagePassesShouldIncreaseQualityByThreeWhenLessThanFiveDaysRemain() {
        Item backstagePasses = new Item("Backstage passes", 4, 10);

        Item result = service.updateItem(backstagePasses);

        assertEquals(3, result.sellIn());
        assertEquals(13, result.quality());
    }

    @Test
    public void backstagePassesShouldIncreaseQualityByThreeWhenFiveDaysRemain() {
        Item backstagePasses = new Item("Backstage passes", 5, 10);

        Item result = service.updateItem(backstagePasses);

        assertEquals(4, result.sellIn());
        assertEquals(13, result.quality());
    }

    @Test
    public void backstagePassesShouldIncreaseQualityByTwoWhenTenDaysRemain() {
        Item backstagePasses = new Item("Backstage passes", 10, 10);

        Item result = service.updateItem(backstagePasses);

        assertEquals(9, result.sellIn());
        assertEquals(12, result.quality());
    }

    @Test
    public void shouldUpdateEntireInventory() {
        List<Item> items = List.of(
                new Item("Normal Item", 5, 10),
                new Item("Aged Brie", 3, 20),
                new Item("Sulfuras", 0, 80),
                new Item("Backstage passes", 5, 10)
        );

        List<Item> updatedList = service.updateInventory(items);

        assertEquals(
                List.of(
                        new Item("Normal Item", 4, 9),
                        new Item("Aged Brie", 2, 21),
                        new Item("Sulfuras", 0, 80),
                        new Item("Backstage passes", 4, 13)
                ),
                updatedList
        );
    }

    @Test
    public void shouldReturnEmptyListWhenInventoryIsEmpty() {
        List<Item> updatedList = service.updateInventory(List.of());

        assertEquals(List.of(), updatedList);
    }

    @Test
    public void shouldRejectItemWithNullName() {
        Item item = new Item(null, 5, 10);

        InvalidInventoryException exception = assertThrows(
                InvalidInventoryException.class,
                () -> service.updateItem(item)
        );

        assertEquals(
                "Item name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    public void shouldUpdateInventoryUsingSisAssessmentExample() {
        List<Item> items = List.of(
                new Item("Aged Brie", 1, 1),
                new Item("Backstage passes", -1, 2),
                new Item("Backstage passes", 9, 2),
                new Item("Sulfuras", 0, 80),
                new Item("Normal Item", -1, 55),
                new Item("Normal Item", 2, 2),
                new Item("Conjured", 2, 2),
                new Item("Conjured", -1, 5)
        );

        List<Item> expectedList = List.of(
                new Item("Aged Brie", 0, 2),
                new Item("Backstage passes", -2, 0),
                new Item("Backstage passes", 8, 4),
                new Item("Sulfuras", 0, 80),
                new Item("Normal Item", -2, 50),
                new Item("Normal Item", 1, 1),
                new Item("Conjured", 1, 0),
                new Item("Conjured", -2, 1)
        );

        List<Item> actualList = service.updateInventory(items);

        assertEquals(expectedList, actualList);


    }
}
