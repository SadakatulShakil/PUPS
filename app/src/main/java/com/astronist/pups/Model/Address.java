package com.astronist.pups.Model;

import java.io.Serializable;

public class Address implements Serializable {
    private String userId;
    private String customerName;
    private String customerPhone;
    private String customerDohsName;
    private String customerHouseNo;
    private String customerRoadNo;

    public Address() {
    }

    public Address(String userId, String customerName, String customerPhone,
                   String customerDohsName, String customerHouseNo, String customerRoadNo) {
        this.userId = userId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerDohsName = customerDohsName;
        this.customerHouseNo = customerHouseNo;
        this.customerRoadNo = customerRoadNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerDohsName() {
        return customerDohsName;
    }

    public void setCustomerDohsName(String customerDohsName) {
        this.customerDohsName = customerDohsName;
    }

    public String getCustomerHouseNo() {
        return customerHouseNo;
    }

    public void setCustomerHouseNo(String customerHouseNo) {
        this.customerHouseNo = customerHouseNo;
    }

    public String getCustomerRoadNo() {
        return customerRoadNo;
    }

    public void setCustomerRoadNo(String customerRoadNo) {
        this.customerRoadNo = customerRoadNo;
    }
}
