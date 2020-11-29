package com.astronist.pups.Model;

public class Order {
    private String userId;
    private String time;
    private String date;
    private String name;
    private String contact;
    private String location;
    private String productName;
    private String unit;
    private String currency;
    private String productQuantity;
    private String totalPrice;

    public Order() {
    }

    public Order(String userId, String time,
                 String date, String name,
                 String contact, String location,
                 String productName, String unit,
                 String currency, String productQuantity,
                 String totalPrice) {
        this.userId = userId;
        this.time = time;
        this.date = date;
        this.name = name;
        this.contact = contact;
        this.location = location;
        this.productName = productName;
        this.unit = unit;
        this.currency = currency;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
