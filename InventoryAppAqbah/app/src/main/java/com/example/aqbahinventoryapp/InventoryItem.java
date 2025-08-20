package com.example.aqbahinventoryapp;

public class InventoryItem {
    public long id;
    public String name;
    public String sku;
    public int quantity;
    public int threshold;

    public InventoryItem(long id, String name, String sku, int quantity, int threshold) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.quantity = quantity;
        this.threshold = threshold;
    }
}
