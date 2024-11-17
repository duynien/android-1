package com.example.btl_android.model;

import java.io.Serializable;

public class Cart implements Serializable {
    private int id;
    private Product product;
    private User user;
    private int quantity;
    public Cart(){

    }

    public Cart(int id, Product product, User user, int quantity) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }

    public Cart(Product product, User user, int quantity) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", product=" + product +
                ", user=" + user +
                ", quantity=" + quantity +
                '}';
    }
}
