package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/27/2015.
 */
public enum ProjectStatus {
    Create("Please Select Project Status", 0),
    New("New", 1),
    Match("Match", 2),
    Pick("Pick", 3),
    CandidateNotification("Candidate Notification", 4),
    ComfirmRequest("Comfirm Request", 5),
    Quotation("Quotation", 6),
    ConfirmQuotation("Confirm Quotation", 7),
    DoDownPayment("Do down payment", 8),
    WinAwardNotification("Win award notification", 9),
    ReceiveDownPayment("Receive Down payment", 10),
    ServiceStart("Service Start", 11),
    ServiceDone("Service Done", 12),
    CustomerRating("Customer Rating", 13),              //Customer give service provider rating
    ServiceProvRating("Service Provider Rating", 14),   //Provider service give customer rating
    Done("Done", 15);

    private String stringValue;
    private int intValue;
    private ProjectStatus(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getId(){
        return intValue;
    }

//    convert From Int To Enum
//    ProjectStatus.values()[productId];
//
//    convert From
}
