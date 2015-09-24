package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.AboutUs;
import com.example.chungmin.helpu.activities.ContactUs;
import com.example.chungmin.helpu.activities.CustomerRequestJobList;
import com.example.chungmin.helpu.activities.CustomerRequestList;
import com.example.chungmin.helpu.activities.Hire;
import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ServiceProviderList;
import com.example.chungmin.helpu.activities.UserProfileSetting;
import com.example.chungmin.helpu.activities.Work;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;

import junit.framework.Assert;

import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/13/2015.
 */
public class MainActivityTest {
    Activity mActivity = null;
    String mClassName = "";

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);


    @org.junit.Ignore
    public void isDisplayObjectExist() {
        //TextView
        onView(withText(R.string.strWelcome)).check(matches(isDisplayed()));
        onView(withText(R.string.strTotalRequestHire)).check(matches(isDisplayed()));
        onView(withText(R.string.strTotalServiceProviderWork)).check(matches(isDisplayed()));
        onView(withText(R.string.strJobOffer)).check(matches(isDisplayed()));
        onView(withText(R.string.strJobOfferDone)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.txUsername)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnHire)).check(matches(isDisplayed()));
        onView(withId(R.id.btnWork)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void testOptionMenu() {
        onView(withText(R.string.action_about_us)).check(doesNotExist());
        ActivityUtils.fromActivityToActivity(MainActivity.class, AboutUs.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "AboutUs");

        onView(isRoot())
                .perform(pressBack());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        onView(withText(R.string.action_contact_us)).check(doesNotExist());
        ActivityUtils.fromActivityToActivity(MainActivity.class, ContactUs.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "ContactUs");
    }

    @org.junit.Ignore
    public void isRedirectLinkWorkable() {
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, UserProfileSetting.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "UserProfileSetting");

        ActivityUtils.fromActivityToActivity(UserProfileSetting.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestList.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "CustomerRequestList");

        ActivityUtils.fromActivityToActivity(CustomerRequestList.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, ServiceProviderList.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "ServiceProviderList");

        ActivityUtils.fromActivityToActivity(ServiceProviderList.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, Hire.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Hire");

        ActivityUtils.fromActivityToActivity(Hire.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, Work.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Work");

        ActivityUtils.fromActivityToActivity(Work.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestJobList.class, "JobOffer");

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "CustomerRequestJobList");

        ActivityUtils.fromActivityToActivity(CustomerRequestJobList.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestJobList.class, "JobDone");

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "CustomerRequestJobList");

        ActivityUtils.fromActivityToActivity(CustomerRequestJobList.class, MainActivity.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Login");

        AutoAction.autoLogin("apple", "Password123");

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");


    }
}
