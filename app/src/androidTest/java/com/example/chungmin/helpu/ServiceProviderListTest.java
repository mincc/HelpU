package com.example.chungmin.helpu;

import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ServiceProviderList;
import com.example.chungmin.helpu.activities.Work;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.User;

import org.hamcrest.Matchers;
import org.junit.Rule;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Chung Min on 9/16/2015.
 */
public class ServiceProviderListTest {

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @org.junit.Ignore
    public void process() {

        User user = AutoAction.userLogin(false);
        ActivityUtils.fromActivityToActivity(MainActivity.class, Work.class);
        createSingleWork();

        AutoAction.autoLogout();
        AutoAction.userLogin(false);
        ActivityUtils.fromActivityToActivity(MainActivity.class, Work.class);
        createMultipleWork();

    }

    private void createMultipleWork() {
        //add a few service provider
        List<ServiceProvider> serviceProviderList = AutoAction.autoWorkList();

        AutoAction.goToActivity(Work.class, ServiceProviderList.class);
        //check all the customer request is created by same person
        for (ServiceProvider serviceProviderRec : serviceProviderList) {
            onView(allOf(withText(serviceProviderRec.getEmail())))
                    .check(matches(isDisplayed()));
            onView(allOf(withText(serviceProviderRec.getPhone())))
                    .check(matches(isDisplayed()));
        }
    }

    private void createSingleWork() {
        //add one customer request
        ServiceProvider serviceProvider = AutoAction.autoWork();
        onView(allOf(withText(serviceProvider.getEmail())))
                .check(matches(isDisplayed()));
        onView(allOf(withText(serviceProvider.getPhone())))
                .check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void deleteServiceProvider() {
        int mServiceId = 1;

        //** before run this fuction, make sure no similar service for the user
        //login with existing user
        AutoAction.autoLogout();
        AutoAction.autoLogin("apple", "Password123");

        //create a new service provider
        ActivityUtils.fromActivityToActivity(MainActivity.class, Work.class);
        ServiceProvider serviceProvider = AutoAction.autoWork(false, mServiceId);

        //delete the service provider
        onView(Matchers.allOf(withId(R.id.btnDelete)))
                .perform(click());

        //recreate a service provider with similar service
        AutoAction.goToActivity(ServiceProviderList.class, Work.class);
        serviceProvider = AutoAction.autoWork(false, mServiceId);
    }
}
