package com.example.btl_android.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String image, name, price, description, category;
    public Product(){

    }

    public Product(int id, String image, String name, String price, String describe, String category) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.description = describe;
        this.category = category;
    }
    public Product(String image, String name, String price, String describe, String category) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description = describe;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescribe(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", describe='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
