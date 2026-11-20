package com.example.sklep.model;

import java.security.cert.CertificateNotYetValidException;
import java.util.List;

public class Profile {
    private long id;
    private String name;
    private String surname;
    private String address;
    private Account account;
    private List<CartItem> cart;

    public Profile() {
    }

    public Profile(long id, String name, String surname, String address, Account account, List<CartItem> cart) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.account = account;
        this.cart = cart;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }
}
