package com.example.sklep.model;

import java.util.List;

public class Item {
    private long id;
    private String name;
    private double precision;
    private int count;
    private String description;
    private List<CartItem> cartItems;
    public Item() {
    }

    public Item(long id, String name, double precision, int count, String description, List<CartItem> cartItems) {
        this.id = id;
        this.name = name;
        this.precision = precision;
        this.count = count;
        this.description = description;
        this.cartItems = cartItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
