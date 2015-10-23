package com.example.chungmin.helpu.models;

/**
 * Created by Chung Min on 10/20/2015.
 */
public class ChatTopic {
    int userIdCust, userIdSPdr, customerRequestId;
    String userNameCust, userNameSPdr, description;

    public int getUserIdCust() {
        return userIdCust;
    }

    public void setUserIdCust(int userIdCust) {
        this.userIdCust = userIdCust;
    }

    public int getUserIdSPdr() {
        return userIdSPdr;
    }

    public void setUserIdSPdr(int userIdSPdr) {
        this.userIdSPdr = userIdSPdr;
    }

    public int getCustomerRequestId() {
        return customerRequestId;
    }

    public void setCustomerRequestId(int customerRequestId) {
        this.customerRequestId = customerRequestId;
    }

    public String getUserNameCust() {
        return userNameCust;
    }

    public void setUserNameCust(String userNameCust) {
        this.userNameCust = userNameCust;
    }

    public String getUserNameSPdr() {
        return userNameSPdr;
    }

    public void setUserNameSPdr(String userNameSPdr) {
        this.userNameSPdr = userNameSPdr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
