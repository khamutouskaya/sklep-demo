package com.example.sklep.model;

import java.util.Date;
import java.util.List;

public class Order {
    private long id;
    private Date timestamp;
    private String paymentType;
    private String state;
    private String comment;
    private List<CartItem> cartItems;


    public Order() {
    }

    public Order(long id, Date timestamp, String paymentType, String state, String comment, List<CartItem> cartItems) {
        this.id = id;
        this.timestamp = timestamp;
        this.paymentType = paymentType;
        this.state = state;
        this.comment = comment;
        this.cartItems = cartItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
