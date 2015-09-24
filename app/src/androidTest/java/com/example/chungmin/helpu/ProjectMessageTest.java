package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.chungmin.helpu.activities.CustomerRequestJobList;
import com.example.chungmin.helpu.activities.CustomerRequestList;
import com.example.chungmin.helpu.activities.Hire;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.Work;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;
import com.example.chungmin.helpu.custom.CustomMatchers;
import com.example.chungmin.helpu.custom.CustomViewActions;
import com.example.chungmin.helpu.custom.Utils;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.User;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;

import HelpUTestUtilities.ValueGenerator;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;


/**
 * Created by Chung Min on 9/14/2015.
 */
public class ProjectMessageTest {
    Activity mActivity = null;
    String mClassName = "";
    String mTag = "ProjectMessageTest";
    String message = "";
    User mUserCustomer = new User();
    User mUserServiceProvider = new User();
    ServiceProvider mServiceProvider;
    int mServiceId = ValueGenerator.getRandomInt((short) 1, (short) 10);
    int mCustomerRequestId = 0; //0;
    String mProjectStatus = "";
    String mRandomPrice = "";
    double mRandomRatingValue = 0.0;
    int mTimer = 25 * 1000;
    boolean mIsPartialTest = false;

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @org.junit.Ignore
    public void isDisplayObjectExist() {
//        //TextView
//        onView(withText(R.string.strWelcome)).check(matches(isDisplayed()));
//        onView(withText(R.string.strTotalRequestHire)).check(matches(isDisplayed()));
//        onView(withText(R.string.strTotalServiceProviderWork)).check(matches(isDisplayed()));
//        onView(withText(R.string.strJobOffer)).check(matches(isDisplayed()));
//        onView(withText(R.string.strJobOfferDone)).check(matches(isDisplayed()));
//
//        //EditText
//        onView(withId(R.id.etUsername)).check(matches(isDisplayed()));
//
//        //Button
//        onView(withId(R.id.btnHire)).check(matches(isDisplayed()));
//        onView(withId(R.id.btnWork)).check(matches(isDisplayed()));
//        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void process() {
        mIsPartialTest = false;
        mActivity = main.getActivity();

        message = "pre-set service Id : " + mServiceId;
        Log.d(mTag, message);

        //register mUserCustomer
        createCustomerUser();
        //register mUserServiceProvider
        createServiceProviderUser();
        //mUserServiceProvider register service he or she provide
        serviceProviderCreateWork();
        //mUserCustomer want to hire someone (Project Status - New, Match, Pick)
        customerCreateHire();
        //Project Status - Pick, SelectedNotification, ConfirmRequest
        serviceProviderConfirmRequest();
        //Project Status - ConfirmRequestNotification
        customerConfirmRequest();
        //Project Status - Confirm Quotation
        serviceProviderCreateQuotation();
        //Project Status - Confirm Quotation Notification
        customerConfirmQuotation();
        //Project Status - Deal Notification, Plan Start Date
        serviceProviderDeal();
        //Project Status - Plan Start Date Notification
        customerPlanStartDate();
        //Project Status - Start Service
        serviceProviderStartService();
        //Project Status - Service Start Notification, Service End
        customerServiceEnd();
        //Project Status - Service End Notification, Service Provider Rating
        serviceProviderRating();
        //Project Status - Service Provider Rating Notification
        customerRating();
        //Project Status - Customer Rating Notification, Project Close
        serviceProviderProjectDone();
    }

    @org.junit.Ignore
    public void partialProcessTesting() {
        mIsPartialTest = true;
        mCustomerRequestId = 132;
        mActivity = main.getActivity();

        AutoAction.autoLogout();
        //Project Status - Service Provider Rating Notification
        customerRating();
        //Project Status - Customer Rating Notification, Project Close
        serviceProviderProjectDone();
    }

    private void loginUser(String type, boolean isPartialTest) {
        if (type == "Customer") {
            if (isPartialTest == true) {
                AutoAction.autoLogin("Krysten59", "1SJ2T1s");
            } else {
                AutoAction.autoLogin(mUserCustomer.getUsername(), mUserCustomer.getPassword());
            }
        } else {
            if (isPartialTest == true) {
                AutoAction.autoLogin("Rey63", "g7tjSM8");
            } else {
                AutoAction.autoLogin(mUserServiceProvider.getUsername(), mUserServiceProvider.getPassword());
            }
        }
    }

    private void serviceProviderProjectDone() {
        Log.d(mTag, "serviceProviderProjectDone");
        loginUser("ServiceProvider", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not (Customer Rating Notification...)
        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestJobList.class, "JobDone");
        onView(Matchers.allOf(withText(R.string.strEnter), hasSibling(withText(mCustomerRequestId + ""))))
                .perform(click());

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strPrjClose), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void customerRating() {
        Log.d(mTag, "customerRating");
        loginUser("Customer", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not (Service Provider Rating ...)
        custGoToTargetCR();

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strSvcPdrRtgNtf), mProjectStatus);
        mRandomRatingValue = Math.round(ValueGenerator.getRandomDoubleNumber(0.5, 5.0) - 0.5) + 0.5;
        onView(withId(R.id.ratingBar)).perform(CustomViewActions.setRating(mRandomRatingValue));
        onView(withId(R.id.btnSubmit)).perform(scrollTo(), click());

        custGoToTargetCR();
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strCustRtg), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void serviceProviderRating() {
        Log.d(mTag, "serviceProviderRating");
        loginUser("ServiceProvider", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not (Service Done...)
        spdrGoToTargetCR();

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strSvcDoneNtf), mProjectStatus);
        //round to nearest 0.5
        mRandomRatingValue = Math.round(ValueGenerator.getRandomDoubleNumber(0.5, 5.0) - 0.5) + 0.5;
        onView(withId(R.id.ratingBar)).perform(CustomViewActions.setRating(mRandomRatingValue));
        onView(withId(R.id.btnSubmit)).perform(scrollTo(), click());

        spdrGoToTargetCR();
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strSPdrRtg), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void customerServiceEnd() {
        Log.d(mTag, "customerServiceEnd");
        loginUser("Customer", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not (Service Start...)
        custGoToTargetCR();

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strSvcSttNtf), mProjectStatus);
        onView(withId(R.id.btnTask))
                .perform(click());

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strSvcDone), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void serviceProviderStartService() {
        Log.d(mTag, "serviceProviderStartService");
        loginUser("ServiceProvider", mIsPartialTest);
        spdrGoToTargetCR();

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strPlanStartDateNtf), mProjectStatus);
        onView(withId(R.id.btnTask))
                .perform(click());

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strSvcStr), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void customerPlanStartDate() {
        Log.d(mTag, "customerPlanStartDate");
        loginUser("Customer", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not
        custGoToTargetCR();

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strPlanStartDateNtf), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void serviceProviderDeal() {
        Log.d(mTag, "serviceProviderDeal");
        loginUser("ServiceProvider", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not
        spdrGoToTargetCR();
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strDealNtf), mProjectStatus);
        onView(withId(R.id.btnTask))
                .perform(click());
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strPlanStrtDate), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void customerConfirmQuotation() {
        Log.d(mTag, "customerConfirmQuotation");
        loginUser("Customer", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not
        custGoToTargetCR();

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strCfmQttNtf), mProjectStatus);
        onView(withId(R.id.btnTask))
                .perform(click());

        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strDeal), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void serviceProviderCreateQuotation() {
        Log.d(mTag, "serviceProviderCreateQuotation");
        loginUser("ServiceProvider", mIsPartialTest);
        spdrGoToTargetCR();
        mRandomPrice = String.valueOf(ValueGenerator.getRandomDoubleNumber((double) 100.00, (double) 200.00));
        onView(withId(R.id.etQuotation)).perform(typeText(mRandomPrice), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnDone)).perform(click());

        String actualPrice = Utils.getText(withId(R.id.tvQuotation));
        Assert.assertEquals(mRandomPrice, actualPrice);
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strCfmQtt), mProjectStatus);

        AutoAction.autoLogout();
    }

    private void customerConfirmRequest() {
        Log.d(mTag, "customerConfirmRequest");
        loginUser("Customer", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not
        custGoToTargetCR();
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strCfmRqtNtf), mProjectStatus);
        AutoAction.autoLogout();
    }

    private void serviceProviderConfirmRequest() {
        Log.d(mTag, "serviceProviderConfirmRequest");
        loginUser("ServiceProvider", mIsPartialTest);
        sleep(mTimer); //check notification trigger or not
        spdrGoToTargetCR();
        onView(withId(R.id.btnTask)).perform(click());
        mProjectStatus = Utils.getText(withId(R.id.tvProjectStatus));
        Assert.assertEquals(mActivity.getString(R.string.strCfmRqt), mProjectStatus);
        AutoAction.autoLogout();
    }

    public void createCustomerUser() {
        Log.d(mTag, "createCustomerUser - register mUserCustomer");
        mUserCustomer = AutoAction.userLogin(false);
        message = "mUserCustomer username : " + mUserCustomer.getUsername();
        Log.d(mTag, message);
        message = "mUserCustomer password : " + mUserCustomer.getPassword();
        Log.d(mTag, message);
    }

    public void createServiceProviderUser() {
        Log.d(mTag, "createServiceProviderUser - register mUserServiceProvider");
        mUserServiceProvider = AutoAction.userLogin(false);
        message = "mUserServiceProvider username : " + mUserServiceProvider.getUsername();
        Log.d(mTag, message);
        message = "mUserServiceProvider password : " + mUserServiceProvider.getPassword();
        Log.d(mTag, message);
    }

    public void serviceProviderCreateWork() {
        Log.d(mTag, "serviceProviderCreateWork - mUserServiceProvider register service he or she provide");
        AutoAction.goToActivity(MainActivity.class, Work.class);
        mServiceProvider = AutoAction.autoWork(false, mServiceId);
        message = "mUserServiceProvider email : " + mServiceProvider.getEmail();
        Log.d(mTag, message);
        message = "mUserServiceProvider phone : " + mServiceProvider.getPhone();
        Log.d(mTag, message);
        message = "mUserServiceProvider service Id: " + mServiceProvider.getServiceId();
        Log.d(mTag, message);
        AutoAction.autoLogout();
    }

    private void customerCreateHire() {
        Log.d(mTag, "customerCreateHire - mUserCustomer want to hire someone");
        AutoAction.autoLogin(mUserCustomer.getUsername(), mUserCustomer.getPassword());
        AutoAction.goToActivity(MainActivity.class, Hire.class);
        CustomerRequest customerRequest = AutoAction.autoHire(false, mServiceId);
        message = "mUserCustomer service Id : " + customerRequest.getServiceId();
        Log.d(mTag, message);
        message = "mUserCustomer description : " + customerRequest.getDescription();
        Log.d(mTag, message);
        onData(CustomMatchers.serviceProviderWithEmail(mServiceProvider.getEmail()))
                .perform(click());
//        onData(allOf(instanceOf(ServiceProvider.class),
//                (Matcher<? super Object>) CustomMatchers.serviceProviderWithEmail(mServiceProvider.getEmail())))
//                .perform(click());
        mCustomerRequestId = Integer.parseInt(Utils.getText(withId(R.id.tvCustomerRequestId)));
        AutoAction.autoLogout();
    }

    private void spdrGoToTargetCR() {
        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestJobList.class, "JobOffer");
        onView(Matchers.allOf(withText(R.string.strEnter), hasSibling(withText(mCustomerRequestId + ""))))
                .perform(click());
    }

    private void custGoToTargetCR() {
        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestList.class);
        onView(Matchers.allOf(withText(R.string.strEnter), hasSibling(withText(mCustomerRequestId + ""))))
                .perform(click());
    }
}
