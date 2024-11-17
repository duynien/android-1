package com.example.btl_android.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private int id;
    private User user;
    private Product product;
    private int rating;
    private String comment, date;

    public Comment(int id, User user, Product product, int rating, String comment, String date) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", product=" + product +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
