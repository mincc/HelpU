package com.example.chungmin.helpu.ui_test;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.Register;
import com.example.chungmin.helpu.activities.ServiceProviderList;
import com.example.chungmin.helpu.activities.Work;
import com.example.chungmin.helpu.ui_test.common.ActivityUtils;
import com.example.chungmin.helpu.ui_test.common.AutoAction;
import com.example.chungmin.helpu.ui_test.custom.CustomMatchers;
import com.example.chungmin.helpu.models.User;

import junit.framework.Assert;

import org.junit.Rule;

import HelpUTestUtilities.ValueGenerator;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/14/2015.
 */
public class WorkTest {
    Activity mActivity = null;
    String mClassName = "";

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
    private int mRandomServiceId;
    private String mRandomContact;
    private String mRandomEmail;

    @org.junit.Ignore
    public void isDisplayObjectExist() {
        AutoAction.InitialSetting(Work.class);

        //TextView
        onView(ViewMatchers.withText(R.string.strWorkSubTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.strType)).check(matches(isDisplayed()));
        onView(withText(R.string.strContact)).check(matches(isDisplayed()));
        onView(withText(R.string.strEmail)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.etContact)).check(matches(isDisplayed()));
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));

        //Spinner
        onView(withId(R.id.spWork)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void WorkProcess() {
        AutoAction.InitialSetting(Work.class);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Work");

        //fail to register
        fail();

    }

    public void fail() {
        //region case 1 : cancel activity
        onView(isRoot())
                .perform(pressBack());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

        onView(withId(R.id.btnWork))
                .perform(click());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Work");
        //endregion

        //region case 2 : Type selected only
        mRandomServiceId = ValueGenerator.getRandomInt(1, 10);
        onView(withId(R.id.spWork))
                .perform(click());
        onData(CustomMatchers.withServiceId(mRandomServiceId))
                .perform(click());
        onView(withId(R.id.btnRegister))
                .perform(click());
        onView(withId(R.id.etContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strContactEmpty))));
        onView(withId(R.id.etEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strEmailEmpty))));
        resetAllInput();
        //endregion

        //region case 3 : Contact input only
        mRandomContact = ValueGenerator.getRandomMobileNumber();
        onView(withId(R.id.etContact))
                .perform(typeText(mRandomContact), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister))
                .perform(click());

        onView(withId(R.id.lblType))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strServiceEmpty))));
        onView(withId(R.id.etEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strEmailEmpty))));
        resetAllInput();
        //endregion

        //region case 4 : Email input only
        mRandomEmail = ValueGenerator.getRandomEmailAddress();
        onView(withId(R.id.etEmail))
                .perform(typeText(mRandomEmail), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister))
                .perform(click());

        onView(withId(R.id.lblType))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strServiceEmpty))));
        onView(withId(R.id.etContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strContactEmpty))));
        resetAllInput();
        //endregion

        //region case 5 : Email invalid
        mRandomEmail = ValueGenerator.getRandomString((short) 10, (short) 15);
        onView(withId(R.id.etEmail))
                .perform(typeText(mRandomEmail), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister))
                .perform(click());

        onView(withId(R.id.lblType))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strServiceEmpty))));
        onView(withId(R.id.etContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strContactEmpty))));
        onView(withId(R.id.etEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strEmailInvalid))));
        resetAllInput();
        //endregion

        //region case 6 : service already provide by user
        AutoAction.autoLogout();
        ActivityUtils.fromActivityToActivity(Login.class, Register.class);
        User user = AutoAction.registerNewUser();
        AutoAction.autoLogin(user.getUsername(), user.getPassword());
        ActivityUtils.fromActivityToActivity(MainActivity.class, Work.class);
        success();
        AutoAction.goToActivity(ServiceProviderList.class, Work.class);
        onView(withId(R.id.spWork))
                .perform(click());
        onData(CustomMatchers.withServiceId(mRandomServiceId))
                .perform(click());
        onView(withId(R.id.btnRegister))
                .perform(click());

        onView(withId(R.id.lblType))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strServiceAlreadyExist))));
        onView(withId(R.id.etContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strContactEmpty))));
        onView(withId(R.id.etEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strEmailEmpty))));
        resetAllInput();
        //endregion

    }

    public void success() {
        mRandomServiceId = ValueGenerator.getRandomInt((short) 1, (short) 10);
        mRandomContact = ValueGenerator.getRandomMobileNumber();
        mRandomEmail = ValueGenerator.getRandomEmailAddress();

        onView(withId(R.id.spWork)).perform(click());
        onData(CustomMatchers.withServiceId(mRandomServiceId))
                .perform(click());
        onView(withId(R.id.etContact))
                .perform(typeText(mRandomContact), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etEmail))
                .perform(typeText(mRandomEmail), ActivityUtils.closeSoftKeyboard());

        onView(withId(R.id.btnRegister))
                .perform(click());
    }

    private void resetAllInput() {
        onView(withId(R.id.etContact)).perform(clearText());
        onView(withId(R.id.etEmail)).perform(clearText());
        onView(withId(R.id.spWork)).perform(click());
        onData(CustomMatchers.withServiceName(mActivity.getString(R.string.strPlsSlct)))
                .perform(click());
    }

}
