package com.astronist.pups.Model;

import java.io.Serializable;

public class CartList implements Serializable {
    private String userId;
    private String pushId;
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
    private String category;
    private String deliveryStatus;
    public CartList() {
    }

    public CartList(String userId, String pushId,
                    String time, String date, String name,
                    String contact, String location,
                    String productName, String unit,
                    String currency, String productQuantity,
                    String totalPrice, String category, String deliveryStatus) {
        this.userId = userId;
        this.pushId = pushId;
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
        this.category = category;
        this.deliveryStatus = deliveryStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
