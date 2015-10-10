package com.example.chungmin.helpu.ui_test;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.Hire;
import com.example.chungmin.helpu.ui_test.common.ActivityUtils;
import com.example.chungmin.helpu.ui_test.common.AutoAction;
import com.example.chungmin.helpu.ui_test.custom.CustomMatchers;

import junit.framework.Assert;

import org.junit.Rule;

import HelpUTestUtilities.ValueGenerator;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Chung Min on 9/14/2015.
 */
public class HireTest {
    Activity mActivity = null;
    String mClassName = "";

    static int mRandomService;
    static String mRandomDescription;

    @Rule
    public final ActivityTestRule<Hire> main = new ActivityTestRule<>(Hire.class);


    @org.junit.Ignore
    public void isDisplayObjectExist() {
        AutoAction.autoLogout();
        AutoAction.userLogin(true);

        //Text View
        onView(ViewMatchers.withText(R.string.strHireSubTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.strServiceNeeded)).check(matches(isDisplayed()));
        onView(withText(R.string.strMoreDescription)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.etDescription)).check(matches(isDisplayed()));

        //Spinner
        onView(withId(R.id.spHire)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnOk)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCancel)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void process() {
        AutoAction.autoLogout();
        AutoAction.userLogin(true);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Hire");

        fallToRegisterHireActivity();
        successfulToRegisterHireActivity();

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "ServiceProviderListByServiceID");

    }

    public void fallToRegisterHireActivity() {
        //region case 1 : cancel the activity
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Hire");

        onView(withId(R.id.btnCancel)).perform(click());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        onView(withId(R.id.btnHire)).perform(click());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Hire");
        //endregion

        //region case 2 : no detail
        onView(withId(R.id.btnOk)).perform(click());
        onView(withId(R.id.etDescription))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strDescriptionEmpty))));
        onView(withId(R.id.tvServiceNeeded))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strServiceEmpty))));
        resetAllInput();
        //endregion

        //region case 3 : service select only
        mRandomService = ValueGenerator.getRandomInt(1, 10);
        onView(withId(R.id.spHire)).perform(click());
        onData(CustomMatchers.withServiceId(mRandomService))
                .perform(click());
        onView(withId(R.id.btnOk)).perform(click());
        onView(withId(R.id.etDescription))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strDescriptionEmpty))));
        resetAllInput();
        //endregion

        //region case 4 : description fill only
        mRandomDescription = ValueGenerator.getRandomComment();
        onView(withId(R.id.etDescription))
                .perform(typeText(mRandomDescription), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnOk)).perform(click());
        onView(withId(R.id.tvServiceNeeded))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strServiceEmpty))));
        resetAllInput();
        //endregion

    }

    public void successfulToRegisterHireActivity() {
        mRandomService = ValueGenerator.getRandomInt(1, 10);
        mRandomDescription = ValueGenerator.getRandomComment();
        onView(withId(R.id.spHire)).perform(click());
        onData(CustomMatchers.withServiceId(mRandomService))
                .perform(click());
        onView(withId(R.id.etDescription))
                .perform(typeText(mRandomDescription), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnOk)).perform(click());
    }

    private void resetAllInput() {
        onView(withId(R.id.etDescription)).perform(clearText());
        onView(withId(R.id.spHire)).perform(click());
        onData(CustomMatchers.withServiceName(mActivity.getString(R.string.strPlsSlct)))
                .perform(click());
    }

}
