package com.astronist.pups.Model;

import android.widget.ImageView;

public class Product {
    private int profileImage;
    private String titleName;
    private String price;
    private String status;

    public Product() {
    }

    public Product(int profileImage, String titleName, String price, String status) {
        this.profileImage = profileImage;
        this.titleName = titleName;
        this.price = price;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
