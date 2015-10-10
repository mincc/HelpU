package com.example.chungmin.helpu.models;

import com.example.chungmin.helpu.R;


/**
 * Created by Chung Min on 9/25/2015.
 */
public class AppLink {
    private static String mServer = Globals.getAppContext().getResources().getString(R.string.server_uri);

    //region Customer
    public static String getCustomerRequestHireListGetByUserIDUrl() {
        return mServer + "CustomerRequestHireListGetByUserID.php";
    }

    public static String getCustomerRequestGetByIDUrl() {
        return mServer + "CustomerRequestGetByID.php";
    }

    public static String getCustomerRequestInsertUrl() {
        return mServer + "CustomerRequestInsert.php";
    }

    public static String getCustomerRequestUpdateUrl() {
        return mServer + "CustomerRequestUpdate.php";
    }

    public static String getCustomerRequestJobListGetByUserIDUrl() {
        return mServer + "CustomerRequestJobListGetByUserID.php";
    }

    public static String getCustomerRequestJobDoneListGetByUserIDUrl() {
        return mServer + "CustomerRequestJobDoneListGetByUserID.php";
    }

    public static String getCustomerRequestNotificationTriggerUrl() {
        return mServer + "CustomerRequestNotificationTrigger.php";
    }

    public static String getCustomerRequestJobOfferUrl() {
        return mServer + "CustomerRequestJobOffer.php";
    }
    //endregion

    //region Service Provider
    public static String getServiceProviderGetByIDUrl() {
        return mServer + "ServiceProviderGetByID.php";
    }

    public static String getServiceProviderGetByUserIDUrl() {
        return mServer + "ServiceProviderGetByUserID.php";
    }

    public static String getServiceProviderGetByServiceIDUrl() {
        return mServer + "ServiceProviderGetByServiceID.php";
    }

    public static String getServiceProviderInsertUrl() {
        return mServer + "ServiceProviderInsert.php";
    }

    public static String serviceProviderDeleteUrl() {
        return mServer + "ServiceProviderDelete.php";
    }

    public static String serviceProviderIsServiceAlreadyExistsUrl() {
        return mServer + "ServiceProviderIsServiceAlreadyExists.php";
    }
    //endregion

    //region User
    public static String getUserGetByUsernameAndPasswordUrl() {
        return mServer + "UserGetByUsernameAndPassword.php";
    }

    public static String getUserInsertUrl() {
        return mServer + "UserInsert.php";
    }

    public static String getUserUpdateUrl() {
        return mServer + "UserUpdate.php";
    }

    public static String getIsUsernameAlreadyExistsUrl() {
        return mServer + "UserIsUsernameAlreadyExists.php";
    }

    public static String getUserIsCurrentPasswordValidUrl() {
        return mServer + "UserIsCurrentPasswordValid.php";
    }

    public static String getUserPasswordUpdateUrl() {
        return mServer + "UserUpdatePassword.php";
    }

    public static String getUserUpdatePasswordByEmailUrl() {
        return mServer + "UserUpdatePasswordByEmail.php";
    }

    public static String getUserLogoutUrl() {
        return mServer + "UserLogout.php";
    }
    //endregion

    //region Rating
    public static String getRatingInsertUrl() {
        return mServer + "RatingInsert.php";
    }
    //endregion

    //region Tansaction
    public static String getTransactionInsertUrl() {
        return mServer + "TransactionInsert.php";
    }
    //endregion

    //region Customer Issue
    public static String getCustomerIssueInsertUrl() {
        return mServer + "CustomerIssueInsert.php";
    }

    public static String getCustomerIssueGetByIDUrl() {
        return mServer + "CustomerIssueGetByID.php";
    }

    public static String getCustomerIssueUpdateUrl() {
        return mServer + "CustomerIssueUpdate.php";
    }

    public static String getCustomerIssueDeleteUrl() {
        return mServer + "CustomerIssueDelete.php";
    }
    //endregion

    //region Other
    public static String getAppUrl() {
        return mServer + "app.php";
    }

    public static String getUserStatsInfoUrl() {
        return mServer + "UserStatsInfo.php";
    }

    public static String geAppStatsInfoUrl() {
        return mServer + "AppStatsInfo.php";
    }

    //endregion
}
