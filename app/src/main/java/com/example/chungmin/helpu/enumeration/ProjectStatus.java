package com.example.chungmin.helpu.enumeration;


import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;

/**
 * Created by Chung Min on 7/27/2015.
 */
public enum ProjectStatus {

    Create(Globals.getAppContext().getString(R.string.strSlctProjStat), 0),
    New(Globals.getAppContext().getString(R.string.strNew), 1),
    Match(Globals.getAppContext().getString(R.string.strMatch), 2),
    Pick(Globals.getAppContext().getString(R.string.strPick), 3),
    SelectedNotification(Globals.getAppContext().getString(R.string.strSltNtf), 4),
    ConfirmRequest(Globals.getAppContext().getString(R.string.strCfmRqt), 5),
    Quotation(Globals.getAppContext().getString(R.string.strQtt), 6),
    ConfirmQuotation(Globals.getAppContext().getString(R.string.strCfmQtt), 7),
    //    DoDownPayment("Do down payment", 8),
    Deal(Globals.getAppContext().getString(R.string.strDeal), 8),                           //will comment out when apply payment
    DealNotification(Globals.getAppContext().getString(R.string.strDealNtf), 9),
    ReceiveDownPayment(Globals.getAppContext().getString(R.string.strRcvDownPmt), 10),
    ServiceStart(Globals.getAppContext().getString(R.string.strSvcStr), 11),
    ServiceDone(Globals.getAppContext().getString(R.string.strSvcDone), 12),
    CustomerRating(Globals.getAppContext().getString(R.string.strCustRtg), 13),              //Customer give service provider rating
    ServiceProvRating(Globals.getAppContext().getString(R.string.strSPdrRtg), 14),   //Provider service give customer rating
    ProjectClose(Globals.getAppContext().getString(R.string.strPrjClose), 15),
    //    RemoveFromView(Globals.getAppContext().getString(R.string.strRmvFrmView), 16), //replace with isDelete
    TotallyRemoved(Globals.getAppContext().getString(R.string.strTtlRmv), 17),
    Reselect(Globals.getAppContext().getString(R.string.strRslt), 18),
    ConfirmRequestNotification(Globals.getAppContext().getString(R.string.strCfmRqtNtf), 19),
    QuotationNotification(Globals.getAppContext().getString(R.string.strQttNtf), 20),
    ConfirmQuotationNotification(Globals.getAppContext().getString(R.string.strCfmQttNtf), 21),
    ServiceStartNotification(Globals.getAppContext().getString(R.string.strSvcSttNtf), 22),
    ServiceDoneNotification(Globals.getAppContext().getString(R.string.strSvcDoneNtf), 23),
    CustomerRatingNotification(Globals.getAppContext().getString(R.string.strCtmRtgNtf), 24),
    ServiceProviderRatingNotification(Globals.getAppContext().getString(R.string.strSvcPdrRtgNtf), 25),
    PlanStartDate(Globals.getAppContext().getString(R.string.strPlanStrtDate), 26),
    PlanStartDateNotification(Globals.getAppContext().getString(R.string.strPlanStartDateNtf), 27);

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
