package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/24/2015.
 */
public class Rating {
    private int ratingId	;
    private int voterId;
    private int targetUserId;
    private double ratingValue;
    private String ratingType;
    private int customerRequestId;

    public Rating(){}

    public Rating( int voterId, int targetUserId, double ratingValue, String ratingType, int customerRequestId) {
        this.ratingId = -1;
        this.voterId = voterId;
        this.targetUserId = targetUserId;
        this.ratingValue = ratingValue;
        this.ratingType = ratingType;
        this.customerRequestId = customerRequestId;
    }

    public int getRatingId() {
        return ratingId;
    }
    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }
    public int getVoterId() {
        return voterId;
    }
    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }
    public int getTargetUserId() {
        return targetUserId;
    }
    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }
    public double getRatingValue() {
        return ratingValue;
    }
    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }
    public String getRatingType() {
        return ratingType;
    }
    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }
    public int getCustomerRequestId() {
        return customerRequestId;
    }
    public void setCustomerRequestId(int customerRequestId) {
        this.customerRequestId = customerRequestId;
    }
   
    
}
