package com.example.sklep.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private long id;
    private String name;
    private List<Item> items ;
    public Category() {
        this.id = 0;
        this.name = "";
        this.items =  new ArrayList<Item>();

    } //сделать то же самое везде
    public Category(long id, String name, List<Item> items) {
        this.id = id;
        this.name = name;
        this.items = items;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
