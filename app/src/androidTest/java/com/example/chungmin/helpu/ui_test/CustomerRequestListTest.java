package com.example.chungmin.helpu.ui_test;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.CustomerRequestList;
import com.example.chungmin.helpu.activities.Hire;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ServiceProviderListByServiceID;
import com.example.chungmin.helpu.ui_test.common.ActivityUtils;
import com.example.chungmin.helpu.ui_test.common.AutoAction;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.User;

import org.junit.Rule;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Chung Min on 9/15/2015.
 */
public class CustomerRequestListTest {
    Activity mActivity = null;
    String mClassName = "";

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @org.junit.Ignore
    public void process() {

        User user = AutoAction.userLogin(false);
        createSingleHire();
        createMultipleHire();

    }

    private void createMultipleHire() {
        //add a few customer request
        List<CustomerRequest> customerRequestList = AutoAction.autoHireList();

        //check all the customer request is created by same person
        AutoAction.goToActivity(Hire.class, CustomerRequestList.class);
        for (CustomerRequest customerRequestRec : customerRequestList) {
            onView(allOf(ViewMatchers.withText(R.string.strEnter), hasSibling(withText(customerRequestRec.getDescription()))))
                    .perform(click());
            AutoAction.goToActivity(ServiceProviderListByServiceID.class, CustomerRequestList.class);
        }
    }

    private void createSingleHire() {
        ActivityUtils.fromActivityToActivity(MainActivity.class, Hire.class);

        //add one customer request
        CustomerRequest customerRequest = AutoAction.autoHire();
        AutoAction.goToActivity(ServiceProviderListByServiceID.class, CustomerRequestList.class);
        onView(allOf(withText(R.string.strEnter), hasSibling(withText(customerRequest.getDescription()))))
                .perform(click());
        AutoAction.goToActivity(ServiceProviderListByServiceID.class, Hire.class);
    }


}
