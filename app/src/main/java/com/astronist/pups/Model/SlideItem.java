package com.astronist.pups.Model;

import java.io.Serializable;

public class SlideItem implements Serializable {
    private int profileImage;
    private String titleName;
    private String price;
    private String unit;
    private String currency;
    private String status;

    public SlideItem() {
    }

    public SlideItem(int profileImage, String titleName, String price, String unit, String currency, String status) {
        this.profileImage = profileImage;
        this.titleName = titleName;
        this.price = price;
        this.unit = unit;
        this.currency = currency;
        this.status = status;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
