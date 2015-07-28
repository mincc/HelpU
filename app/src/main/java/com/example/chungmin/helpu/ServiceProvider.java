package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/22/2015.
 */
public class ServiceProvider {
    String phone, email, userName, serviceName;
    int userId, serviceId, serviceProviderId;

    public ServiceProvider() {

    }

    public ServiceProvider(int userId, int serviceId, String phone, String email) {
        this.serviceProviderId = -1;
        this.userId = userId;
        this.serviceId = serviceId;
        this.phone = phone;
        this.email = email;
    }

    public ServiceProvider(int serviceProviderId, int userId, int serviceId, String phone, String email) {
        this.serviceProviderId = serviceProviderId;
        this.userId = userId;
        this.serviceId = serviceId;
        this.phone = phone;
        this.email = email;
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

    //User Table
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    //Service Table
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
