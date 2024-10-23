package com.example.inventorymanagementapp;
public class Item {

    private String itemID;
    private String itemName;
    private double price;
    private int quantity;
    private String description;
    private String category;

    // Constructor
    public Item(String itemID, String itemName, double price, int quantity, String description, String category) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
