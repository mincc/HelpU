package com.example.chungmin.helpu.models;

import android.app.Application;
import android.content.Context;

import com.example.chungmin.helpu.R;

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

    public String translateErrorType(String errorMessage) {
        String msg = "";

        switch (errorMessage) {
            case "Connection Timeout":
                msg = getString(R.string.strConnTimeout);
                break;
            case "Incorrect User Details":
                msg = getString(R.string.strIncorrectUserDetails);
                break;
            case "Invalid Response":
                msg = getString(R.string.strInvalidResponse);
                break;
            case "No Network Connection":
                msg = getString(R.string.strNoNetworkConn);
                break;
            case "No Response From Server":
                msg = getString(R.string.strNoResponseFromServer);
                break;
            case "Email Address Not Exist":
                msg = getString(R.string.strEmailNotExist);
                break;
            default:
                msg = getString(R.string.strUnknownAction) + ":" + errorMessage;
                break;
        }

        return msg;
    }

    //Url List for easy management
    //region Customer
    public static String getCustomerRequestGetByUserIDUrl()
    {
        return "CustomerRequestGetByUserID.php";
    }

    public static String getCustomerRequestGetByIDUrl()
    {
        return "CustomerRequestGetByID.php";
    }

    public static String getCustomerRequestInsertUrl()
    {
        return "CustomerRequestInsert.php";
    }

    public static String getCustomerRequestUpdateUrl()
    {
        return "CustomerRequestUpdate.php";
    }

    public static String getCustomerRequestJobListGetByUserIDUrl()
    {
        return "CustomerRequestJobListGetByUserID.php";
    }

    public static String getCustomerRequestJobDoneListGetByUserIDUrl() {
        return "CustomerRequestJobDoneListGetByUserID.php";
    }

    public static String getCustomerRequestNotificationTriggerUrl() {
        return "CustomerRequestNotificationTrigger.php";
    }

    public static String getCustomerRequestJobOfferUrl() {
        return "CustomerRequestJobOffer.php";
    }
    //endregion

    //region Service Provider
    public static String getServiceProviderGetByIDUrl()
    {
        return "ServiceProviderGetByID.php";
    }

    public static String getServiceProviderGetByUserIDUrl()
    {
        return "ServiceProviderGetByUserID.php";
    }

    public static String getServiceProviderGetByServiceIDUrl()
    {
        return "ServiceProviderGetByServiceID.php";
    }

    public static String getServiceProviderInsertUrl()
    {
        return "ServiceProviderInsert.php";
    }

    public static String serviceProviderDeleteUrl() {
        return "ServiceProviderDelete.php";
    }

    public static String serviceProviderIsServiceAlreadyExistsUrl() {
        return "ServiceProviderIsServiceAlreadyExists.php";
    }
    //endregion

    //region User
    public static String getUserGetByUsernameAndPasswordUrl() {
        return "UserGetByUsernameAndPassword.php";
    }

    public static String getUserInsertUrl() {
        return "UserInsert.php";
    }

    public static String getUserUpdateUrl() {
        return "UserUpdate.php";
    }

    public static String getIsUsernameAlreadyExistsUrl() {
        return "UserIsUsernameAlreadyExists.php";
    }

    public static String getUserIsCurrentPasswordValidUrl() {
        return "UserIsCurrentPasswordValid.php";
    }

    public static String getUserPasswordUpdateUrl() {
        return "UserUpdatePassword.php";
    }

    public static String getUserUpdatePasswordByEmailUrl() {
        return "UserUpdatePasswordByEmail.php";
    }
    //endregion

    //region Rating
    public static String getRatingInsertUrl() {
        return "RatingInsert.php";
    }
    //endregion

    //region Tansaction
    public static String getTransactionInsertUrl() {
        return "TransactionInsert.php";
    }
    //endregion

    //region Other
    public static String getAppUrl()
    {
        return "app.php";
    }

    public static String getUserStatsInfoUrl() {
        return "UserStatsInfo.php";
    }

    public static String geAppStatsInfoUrl() {
        return "AppStatsInfo.php";
    }


    //endregion
}