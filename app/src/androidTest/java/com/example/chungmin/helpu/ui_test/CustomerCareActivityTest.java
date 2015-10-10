package com.example.chungmin.helpu.ui_test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.CustomerCareActivity;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.ui_test.common.ActivityUtils;
import com.example.chungmin.helpu.ui_test.common.AutoAction;
import com.example.chungmin.helpu.ui_test.custom.CustomMatchers;

import junit.framework.Assert;

import org.junit.Rule;

import HelpUTestUtilities.ValueGenerator;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/26/2015.
 */
public class CustomerCareActivityTest {
    Activity mActivity = null;
    private String mClassName = "";

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
    private int mRandomCustomerIssueTypeId;
    private String mRandomSubject;
    private String mRandomDescription;


    @org.junit.Ignore
    public void isDisplayObjectExist() {
        AutoAction.goToActivity(MainActivity.class, CustomerCareActivity.class);

        //TextView
        onView(withText(R.string.strType)).check(matches(isDisplayed()));
        onView(withText(R.string.strSubject)).check(matches(isDisplayed()));
        onView(withText(R.string.strDescription)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.etSubject)).check(matches(isDisplayed()));
        onView(withId(R.id.etDescription)).check(matches(isDisplayed()));

        //Spinner
        onView(withId(R.id.spType)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnCancel)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSubmit)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void process() {
        AutoAction.goToActivity(MainActivity.class, CustomerCareActivity.class);
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "CustomerCareActivity");

        fail();

        success();

        AutoAction.goToActivity(CustomerCareActivity.class, MainActivity.class);
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");
    }

    private void success() {
        mRandomCustomerIssueTypeId = ValueGenerator.getRandomInt(1, 4);
        mRandomSubject = ValueGenerator.getRandomArticleTitle();
        mRandomDescription = ValueGenerator.getRandomComment();

        onView(withId(R.id.spType))
                .perform(click());
        onData(CustomMatchers.withCustomerIssueTypeId(mRandomCustomerIssueTypeId))
                .perform(click());
        onView(withId(R.id.etSubject))
                .perform(typeText(mRandomSubject), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etDescription))
                .perform(typeText(mRandomDescription), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnSubmit))
                .perform(click());
    }

    private void fail() {
        //region case 1 : cancel activity
        onView(withId(R.id.btnCancel))
                .perform(click());
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        AutoAction.goToActivity(MainActivity.class, CustomerCareActivity.class);
        //endregion

        //region case 2 : no fill
        onView(withId(R.id.btnSubmit))
                .perform(click());
        onView(withId(R.id.tvInvisibleError))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strTypeEmpty))));
        onView(withId(R.id.etSubject))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strSubjectEmpty))));
        onView(withId(R.id.etDescription))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strDescriptionEmpty))));
        //endregion

        //region case 3 : choose type only
        mRandomCustomerIssueTypeId = ValueGenerator.getRandomInt(1, 4);

        onView(withId(R.id.spType))
                .perform(click());
        onData(CustomMatchers.withCustomerIssueTypeId(mRandomCustomerIssueTypeId))
                .perform(click());
        onView(withId(R.id.btnSubmit))
                .perform(click());
        onView(withId(R.id.etSubject))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strSubjectEmpty))));
        onView(withId(R.id.etDescription))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strDescriptionEmpty))));
        resetAllInput();
        //endregion

        //region case 4 : fill subject only
        mRandomSubject = ValueGenerator.getRandomArticleTitle();
        onView(withId(R.id.etSubject))
                .perform(typeText(mRandomSubject), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnSubmit))
                .perform(click());
        onView(withId(R.id.tvInvisibleError))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strTypeEmpty))));
        onView(withId(R.id.etDescription))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strDescriptionEmpty))));
        resetAllInput();
        //endregion

        //region case 5 : fill description only
        mRandomDescription = ValueGenerator.getRandomComment();
        onView(withId(R.id.etDescription))
                .perform(typeText(mRandomDescription), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnSubmit))
                .perform(click());
        onView(withId(R.id.tvInvisibleError))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strTypeEmpty))));
        onView(withId(R.id.etSubject))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strSubjectEmpty))));
        resetAllInput();
        //endregion

    }

    private void resetAllInput() {
        onView(withId(R.id.spType))
                .perform(click());
        onData(CustomMatchers.withCustomerIssueType(mActivity.getString(R.string.strPlsSlct)))
                .perform(click());
        onView(withId(R.id.etSubject)).perform(clearText());
        onView(withId(R.id.etDescription)).perform(clearText());
    }
}
