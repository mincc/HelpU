package com.example.chungmin.helpu.models;

import com.example.chungmin.helpu.enumeration.CustomerIssueStatus;
import com.example.chungmin.helpu.enumeration.CustomerIssueTypes;

/**
 * Created by Chung Min on 9/24/2015.
 */
public class CustomerIssue {
    private int customerIssueId;
    private int userId;
    private CustomerIssueTypes customerIssueTypes;
    private CustomerIssueStatus customerIssueStatus;
    private String subject;
    private String description;

    public int getCustomerIssueId() {
        return customerIssueId;
    }

    public void setCustomerIssueId(int customerIssueId) {
        this.customerIssueId = customerIssueId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomerIssueTypes getCustomerIssueTypes() {
        return customerIssueTypes;
    }

    public void setCustomerIssueTypes(CustomerIssueTypes customerIssueTypes) {
        this.customerIssueTypes = customerIssueTypes;
    }

    public void setCustomerIssueTypeId(int customerIssueTypeId) {
        this.customerIssueTypes = CustomerIssueTypes.values()[customerIssueTypeId];
    }

    public int getCustomerIssueTypeId() {
        return customerIssueTypes.getId();
    }

    public CustomerIssueStatus getCustomerIssueStatus() {
        return customerIssueStatus;
    }

    public void setCustomerIssueStatus(CustomerIssueStatus customerIssueStatus) {
        this.customerIssueStatus = customerIssueStatus;
    }

    public void setCustomerIssueStatusId(int customerIssueStatusId) {
        this.customerIssueStatus = CustomerIssueStatus.values()[customerIssueStatusId];
    }

    public int getCustomerIssueStatusId() {
        return customerIssueStatus.getId();
    }

}