package com.example.btl_android.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private User user;
    private int total_price;
    private List<OrderProduct> orderProducts;
    private String mobile, address, status, date;
    public Order(){

    }

    public Order(int id, User user, int total_price, List<OrderProduct> orderProducts, String mobile, String address, String status, String date) {
        this.id = id;
        this.user = user;
        this.total_price = total_price;
        this.orderProducts = orderProducts;
        this.mobile = mobile;
        this.address = address;
        this.date = date;
        this.status = status;
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

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", total_price=" + total_price +
                ", orderProducts=" + orderProducts +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
