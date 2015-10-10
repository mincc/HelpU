package com.example.chungmin.helpu.models;

import org.joda.time.DateTime;

/**
 * Created by Chung Min on 7/22/2015.
 */
public class ServiceProvider {
    private String phone, email, userName, serviceName;
    private int userId, serviceId, serviceProviderId;
    private double avgRatedValue;
    private DateTime lastOnline;

    public ServiceProvider() {

    }

    public ServiceProvider(int userId, int serviceId, String phone, String email) {
        this.serviceProviderId = -1;
        this.userId = userId;
        this.serviceId = serviceId;
        this.phone = phone;
        this.email = email;
        this.avgRatedValue = 2.50;
    }

    public ServiceProvider(int serviceProviderId, int userId, String userName, int serviceId, String serviceName,
                           String phone, String email, double avgRatedValue) {
        this.serviceProviderId = serviceProviderId;
        this.userId = userId;
        this.userName = userName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.phone = phone;
        this.email = email;
        this.avgRatedValue = avgRatedValue;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getServiceId() {
        return serviceId;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceProviderId(){
        return serviceProviderId;
    }
    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public double getAvgRatedValue() {
        return avgRatedValue;
    }
    public void setAvgRatedValue(double avgRatedValue) {
        this.avgRatedValue = avgRatedValue;
    }

    //User Table
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DateTime getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(DateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    //Service Table
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
