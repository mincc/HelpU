package com.example.chungmin.helpu;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Chung Min on 8/14/2015.
 */
public class PaymentInfo {
    String merchantKey, merchantCode, refID, prodDesc, amount, userName, userContact,
            userEmail, remark, country, paymentId, currency, lang, backendPostURL;

    PaymentInfo(User user, CustomerRequest customerRequest, ServiceProvider serviceProvider, String backendPostURL) {

//        merchantKey 	= "HQgUUZLVzg";
//        merchantCode = "M01235";
//        refID 		= "Z001";
//        prodDesc 	= "Galaxy Tab";
//        amount 		= "1.0";
//        userName 	= "Angela";
//        userContact 	= "60123456789";
//        userEmail 	= "angela@ipay.com";
//        remark 		= "cheap cheap";
//        country		= "MY";
//        paymentId ="";
//        currency ="MYR";
//        lang ="ISO-8859-1";
//        this.backendPostURL = "www.google.com";

        merchantKey = "HQgUUZLVzg";
        merchantCode = "M01235";
        refID = "CR" + Integer.toString(customerRequest.getCustomerRequestId());
        prodDesc = customerRequest.getServiceName();
        //testing need to use the value equal or less than one ringgit  //customerRequest.getQuotation();
        amount = "1.0";
        userName = user.getUserName();
        userContact = user.getUserContact();
        userEmail = user.getUserEmail();
        // sample remark "Customer ( CSID:000 Name:ABU ) Pay to Service Provider ( SPID:000 Name:ALI) for Service ( SN: XXX )";
        remark = "Customer ( CSID:" + Integer.toString(customerRequest.getCustomerRequestId()) +
                " Name:" + customerRequest.getUserName() + " ) Pay to Service Provider ( SPID:" +
                Integer.toString(serviceProvider.getServiceProviderId()) + " Name:" + serviceProvider.getUserName() +
                ") for related Service ( SN: " + serviceProvider.getServiceName() + " )";
        country = "MY";
        paymentId = "";
        currency = "MYR";
        lang = "ISO-8859-1";
        this.backendPostURL = backendPostURL;                        //dont know how to received the info, need to reconfirm with support team

    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getRefID() {
        return refID;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public String getAmount() {
        return amount;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getRemark() {
        return remark;
    }

    public String getCountry() {
        return country;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLang() {
        return lang;
    }

    public String getBackendPostURL() {
        return backendPostURL;
    }

}
