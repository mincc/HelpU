package com.example.chungmin.helpu.enumeration;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.Globals;

/**
 * Created by Chung Min on 7/27/2015.
 */
public enum CustomerIssueStatus {
    CreateIssue(Globals.getAppContext().getString(R.string.strCreateIssue), 0),
    NewIssue(Globals.getAppContext().getString(R.string.strNewIssue), 1),
    ActionedIssue(Globals.getAppContext().getString(R.string.strActionedIssue), 2),
    IgnoredIssue(Globals.getAppContext().getString(R.string.strIgnoredIssue), 3),
    ClosedIssue(Globals.getAppContext().getString(R.string.strClosedIssue), 4),
    DeletedIssue(Globals.getAppContext().getString(R.string.strDeletedIssue), 5);

    private String stringValue;
    private int intValue;

    private CustomerIssueStatus(String toString, int value) {
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
