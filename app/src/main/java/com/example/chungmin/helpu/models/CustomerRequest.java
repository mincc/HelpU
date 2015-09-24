package com.example.chungmin.helpu.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.chungmin.helpu.enumeration.ProjectStatus;

/**
 * Created by Chung Min on 7/24/2015.
 * 09 Sep 2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */
public class CustomerRequest implements Parcelable {
    private int customerRequestId;
    private int serviceId;
    private String serviceName;
    private int userId;
    private String userName;
    private String userEmail;
    private String userContact;
    private String description;
    private ProjectStatus projectStatus;
    private String projectStatusName;
    private int serviceProviderId;
    private double quotation;
    private double customerRatingValue;
    private double serviceProviderRatingValue;
    private int alreadyReadNotification;

    public CustomerRequest() {
    }

    public CustomerRequest(int serviceId, int userId, String description, ProjectStatus projectStatus) {
        this.customerRequestId = -1;
        this.userId = userId;
        this.serviceId = serviceId;
        this.description = description;
        this.projectStatus = projectStatus;
        this.serviceProviderId = -1;
        this.quotation = 0.00;
        this.customerRatingValue = 0.0;
        this.serviceProviderRatingValue = 0.0;
        this.alreadyReadNotification = 0;
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
    public void setQuotation(double quotation) {
        this.quotation = quotation;
    }

    //User Table
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
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

    public double getCustomerRatingValue() {
        return customerRatingValue;
    }

    public void setCustomerRatingValue(double customerRatingValue) {
        this.customerRatingValue = customerRatingValue;
    }

    public double getServiceProviderRatingValue() {
        return serviceProviderRatingValue;
    }

    public void setServiceProviderRatingValue(double serviceProviderRatingValue) {
        this.serviceProviderRatingValue = serviceProviderRatingValue;
    }

    public int isAlreadyReadNotification() {
        return alreadyReadNotification;
    }

    public void setAlreadyReadNotification(int alreadyReadNotification) {
        this.alreadyReadNotification = alreadyReadNotification;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.customerRequestId);
        dest.writeInt(this.serviceId);
        dest.writeString(this.serviceName);
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userEmail);
        dest.writeString(this.userContact);
        dest.writeString(this.description);
        dest.writeInt(this.projectStatus == null ? -1 : this.projectStatus.ordinal());
        dest.writeString(this.projectStatusName);
        dest.writeInt(this.serviceProviderId);
        dest.writeDouble(this.quotation);
        dest.writeDouble(this.customerRatingValue);
        dest.writeDouble(this.serviceProviderRatingValue);
        dest.writeInt(this.alreadyReadNotification);
    }

    protected CustomerRequest(Parcel in) {
        this.customerRequestId = in.readInt();
        this.serviceId = in.readInt();
        this.serviceName = in.readString();
        this.userId = in.readInt();
        this.userName = in.readString();
        this.userEmail = in.readString();
        this.userContact = in.readString();
        this.description = in.readString();
        int tmpProjectStatus = in.readInt();
        this.projectStatus = tmpProjectStatus == -1 ? null : ProjectStatus.values()[tmpProjectStatus];
        this.projectStatusName = in.readString();
        this.serviceProviderId = in.readInt();
        this.quotation = in.readDouble();
        this.customerRatingValue = in.readDouble();
        this.serviceProviderRatingValue = in.readDouble();
        this.alreadyReadNotification = in.readInt();
    }

    public static final Creator<CustomerRequest> CREATOR = new Creator<CustomerRequest>() {
        public CustomerRequest createFromParcel(Parcel source) {
            return new CustomerRequest(source);
        }

        public CustomerRequest[] newArray(int size) {
            return new CustomerRequest[size];
        }
    };
}
