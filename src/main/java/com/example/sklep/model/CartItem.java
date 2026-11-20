package com.example.sklep.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CartItem {
    private long id;
    private Profile profile;
    private Item item;
    private Order order;
    private int count;
    public CartItem() {
    }
    public CartItem(long id, Profile profile, Item item, Order order, int count) {
        this.id = id;
        this.profile = profile;
        this.item = item;
        this.order = order;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) { this.profile = profile;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item itemId) {
        this.item = itemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
