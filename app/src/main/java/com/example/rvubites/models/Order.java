package com.example.rvubites.models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String date;
    private double totalAmount;
    private String status;
    private List<CartItem> items;

    public Order() {}

    public Order(String orderId, String date, double totalAmount, String status, List<CartItem> items) {
        this.orderId = orderId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public String getDate() { return date; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public List<CartItem> getItems() { return items; }
}