package com.example.chungmin.helpu;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chung Min on 7/28/2015.
 */
public class Globals extends Application {
    private CustomerRequest customerRequest;
    private ServiceProvider serviceProvider;
    private int userId;
    private int mBadgeCount;
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Globals.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Globals.context;
    }

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

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBadgeCount() {
        if (mBadgeCount == 0) {
            //0 will remove the icon number
            mBadgeCount += 1;
        } else {
            mBadgeCount++;
        }
        return mBadgeCount;
    }

    public void setBadgeCount(int mBadgeCount) {
        this.mBadgeCount = mBadgeCount;
    }


    //Url List for easy management
    //region Customer
    public static String getCustomerRequestGetByUserID()
    {
        return "CustomerRequestGetByUserID.php";
    }

    public static String getCustomerRequestGetByID()
    {
        return "CustomerRequestGetByID.php";
    }

    public static String getCustomerRequestInsert()
    {
        return "CustomerRequestInsert.php";
    }

    public static String getCustomerRequestUpdate()
    {
        return "CustomerRequestUpdate.php";
    }

    public static String getCustomerRequestJobListGetByUserID()
    {
        return "CustomerRequestJobListGetByUserID.php";
    }

    public static String getCustomerRequestJobDoneListGetByUserID() {
        return "CustomerRequestJobDoneListGetByUserID.php";
    }

    public static String getCustomerRequestNotificationTrigger() {
        return "CustomerRequestNotificationTrigger.php";
    }

    public static String getCustomerRequestJobOffer() {
        return "CustomerRequestJobOffer.php";
    }
    //endregion

    //region Service Provider
    public static String getServiceProviderGetByID()
    {
        return "ServiceProviderGetByID.php";
    }

    public static String getServiceProviderGetByUserID()
    {
        return "ServiceProviderGetByUserID.php";
    }

    public static String getServiceProviderGetByServiceID()
    {
        return "ServiceProviderGetByServiceID.php";
    }

    public static String getServiceProviderInsert()
    {
        return "ServiceProviderInsert.php";
    }

    public static String serviceProviderDelete() {
        return "ServiceProviderDelete.php";
    }

    public static String serviceProviderIsServiceAlreadyExists() {
        return "ServiceProviderIsServiceAlreadyExists.php";
    }
    //endregion

    //region User
    public static String getUserGetByUsernameAndPassword() {
        return "UserGetByUsernameAndPassword.php";
    }

    public static String getUserInsert() {
        return "UserInsert.php";
    }

    public static String getUserUpdate() {
        return "UserUpdate.php";
    }

    public static String getIsUsernameAlreadyExists() {
        return "UserIsUsernameAlreadyExists.php";
    }
    //endregion

    //region Rating
    public static String getRatingInsert() {
        return "RatingInsert.php";
    }
    //endregion

    //region Tansaction
    public static String getTransactionInsert() {
        return "TransactionInsert.php";
    }
    //endregion

    //Other
    public static String getApp()
    {
        return "app.php";
    }

    public static String getUserStatsInfo() {
        return "UserStatsInfo.php";
    }

    public static String geAppStatsInfo() {
        return "AppStatsInfo.php";
    }
}