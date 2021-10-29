package com.example.market;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int value;

    public Product(int id, String name, int value){
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Product(String name, int value){
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
