package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 8/29/2015.
 */
public class UserStats {
    private int totalCustomerRequest, totalServiceProvider, totalJobOffer, totalJobDone,
            totalWorkNotification, totalHireNotification;

    public UserStats() {

    }

    public UserStats(int totalCustomerRequest, int totalServiceProvider, int totalJobOffer, int totalJobDone,
                     int totalWorkNotification, int totalHireNotification) {
        this.totalCustomerRequest = totalCustomerRequest;
        this.totalServiceProvider = totalServiceProvider;
        this.totalJobOffer = totalJobOffer;
        this.totalJobDone = totalJobDone;
        this.totalWorkNotification = totalWorkNotification;
        this.totalHireNotification = totalHireNotification;
    }

    public int getTotalCustomerRequest() {
        return totalCustomerRequest;
    }

    public void setTotalCustomerRequest(int totalCustomerRequest) {
        this.totalCustomerRequest = totalCustomerRequest;
    }

    public int getTotalServiceProvider() {
        return totalServiceProvider;
    }

    public void setTotalServiceProvider(int totalServiceProvider) {
        this.totalServiceProvider = totalServiceProvider;
    }

    public int getTotalJobOffer() {
        return totalJobOffer;
    }

    public void setTotalJobOffer(int totalJobOffer) {
        this.totalJobOffer = totalJobOffer;
    }

    public int getTotalJobDone() {
        return totalJobDone;
    }

    public void setTotalJobDone(int totalJobDone) {
        this.totalJobDone = totalJobDone;
    }

    public int getTotalWorkNotification() {
        return totalWorkNotification;
    }

    public void setTotalWorkNotification(int totalWorkNotification) {
        this.totalWorkNotification = totalWorkNotification;
    }

    public int getTotalHireNotification() {
        return totalHireNotification;
    }

    public void setTotalHireNotification(int totalHireNotification) {
        this.totalHireNotification = totalHireNotification;
    }
}
