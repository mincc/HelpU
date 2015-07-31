package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/24/2015.
 */
public class CustomerRequest {
    private int customerRequestId;
    private int serviceId;
    private String serviceName;
    private int userId;
    private String userName;
    private String description;
    private ProjectStatus projectStatus;
    private String projectStatusName;
    private int serviceProviderId;
    private double quotation;

    public CustomerRequest() {

    }

    public CustomerRequest(int serviceId, int userId, String description, ProjectStatus projectStatus) {
        this.customerRequestId = -1;
        this.userId = userId;
        this.serviceId = serviceId;
        this.description = description;
        this.projectStatus = projectStatus;
        this.serviceProviderId = -1;
        this.quotation = 0;
    }

    public CustomerRequest(int customerRequestId, int serviceId, String serviceName, int userId, String description,
                           ProjectStatus projectStatus, String userName, String projectStatusName,
                           int serviceProviderId, double quotation) {
        this.customerRequestId = customerRequestId;
        this.userId = userId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.projectStatus = projectStatus;
        this.userName = userName;
        this.projectStatusName = projectStatusName;
        this.serviceProviderId = serviceProviderId;
        this.quotation = quotation;

    }

    public int getCustomerRequestId() {
        return customerRequestId;
    }
    public void setCustomerRequestId(int customerRequestId) {
        this.customerRequestId = customerRequestId;
    }
    public int getServiceId() {
        return serviceId;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }
    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }
    public void setProjectStatusId(int productStatusId) {
        this.projectStatus = ProjectStatus.values()[productStatusId];
    }
    public int getProjectStatusId() {
        return projectStatus.getId();
    }
    public int getServiceProviderId() {
        return serviceProviderId;
    }
    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }
    public double getQuotation() {
        return quotation;
    }
    public void setQuotation(int quotation) {
        this.quotation = quotation;
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

    //Project Status Table
    public String getProjectStatusName() {
        return projectStatusName;
    }
    public void setProjectStatusName(String projectStatusName) {
        this.projectStatusName = projectStatusName;
    }
}
