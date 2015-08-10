package com.example.chungmin.helpu;

import android.app.Application;
/**
 * Created by Chung Min on 7/28/2015.
 */
public class Globals extends Application {
    private CustomerRequest customerRequest;
    private ServiceProvider serviceProvider;
    private Boolean isJobOfferTriggle = false;
    private Boolean isServerAlreadyResponse = false;
    private int userId;

    public CustomerRequest getCustomerRequest() {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerRequest customerRequest) {
        this.customerRequest = customerRequest;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Boolean getIsJobOfferTriggle() {
        return isJobOfferTriggle;
    }

    public void setIsJobOfferTriggle(Boolean isJobOfferTriggle) {
        this.isJobOfferTriggle = isJobOfferTriggle;
    }

    public Boolean getIsServerAlreadyResponse() {
        return isServerAlreadyResponse;
    }

    public void setIsServerAlreadyResponse(Boolean isServerAlreadyResponse) {
        this.isServerAlreadyResponse = isServerAlreadyResponse;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    //Url List for easy management
    //Customer
    public String getCustomerRequestGetByUserID()
    {
        return "CustomerRequestGetByUserID.php";
    }
    public String getCustomerRequestGetByID()
    {
        return "CustomerRequestGetByID.php";
    }
    public String getCustomerRequestInsert()
    {
        return "CustomerRequestInsert.php";
    }
    public String getCustomerRequestGetCount()
    {
        return "CustomerRequestGetCount.php";
    }
    public String getCustomerRequestUpdate()
    {
        return "CustomerRequestUpdate.php";
    }
    public String getCustomerRequestJobListGetByUserID()
    {
        return "CustomerRequestJobListGetByUserID.php";
    }


    //Service Provider
    public String getServiceProviderGetByID()
    {
        return "ServiceProviderGetByID.php";
    }
    public String getServiceProviderGetByUserID()
    {
        return "ServiceProviderGetByUserID.php";
    }
    public String getServiceProviderGetCount()
    {
        return "ServiceProviderGetCount.php";
    }
    public String getServiceProviderGetByServiceID()
    {
        return "ServiceProviderGetByServiceID.php";
    }
    public String getServiceProviderInsert()
    {
        return "ServiceProviderInsert.php";
    }
    public String getServiceProviderIsNotificationTrigger()
    {
        return "ServiceProviderIsNotificationTrigger.php";
    }
    public String getServiceProviderJobOffer()
    {
        return "ServiceProviderJobOffer.php";
    }
    public String getServiceProviderWinAward()
    {
        return "ServiceProviderWinAward.php";
    }
    public String getServiceProviderTotalJobOffer()
    {
        return "ServiceProviderTotalJobOffer.php";
    }


    //User
    public String getUserGetByUsernameAndPassword() {
        return "UserGetByUsernameAndPassword.php";
    }
    public String getUserInsert() {
        return "UserInsert.php";
    }

    //Rating
    public String getRatingInsert() {
        return "RatingInsert.php";
    }

    //Other
    public String getApp()
    {
        return "app.php";
    }
}