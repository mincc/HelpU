package com.example.chungmin.helpu.enumeration;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.Globals;

/**
 * Created by Chung Min on 7/27/2015.
 */
public enum CustomerIssueTypes {
    Create(Globals.getAppContext().getString(R.string.strPlsSlct), 0),
    TechnicalIssue(Globals.getAppContext().getString(R.string.strTechnicalIssue), 1),
    Complaint(Globals.getAppContext().getString(R.string.strComplaint), 2),
    Suggestion(Globals.getAppContext().getString(R.string.strSuggestion), 3),
    Help(Globals.getAppContext().getString(R.string.strHelp), 4);

    private String stringValue;
    private int intValue;

    private CustomerIssueTypes(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getId() {
        return intValue;
    }
}
