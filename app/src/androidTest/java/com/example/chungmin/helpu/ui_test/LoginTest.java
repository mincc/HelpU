package com.example.chungmin.helpu.ui_test;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.ui_test.common.ActivityUtils;
import com.example.chungmin.helpu.ui_test.common.AutoAction;

import junit.framework.Assert;

import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/3/2015.
 */
public class LoginTest {
    Activity mActivity = null;
    String mClassName = "";

    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);
    private String mWrongPassword = "11111";

    @org.junit.Ignore
    public void isDisplayObjectExist() {
        //TextView
        onView(withText("Username")).check(matches(isDisplayed()));
        onView(withText("Password")).check(matches(isDisplayed()));
        onView(withText("Register")).check(matches(isDisplayed()));

        //EditText
        onView(ViewMatchers.withId(R.id.etUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void isRedirectLinkWorkable() {
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Login");

        onView(withText("Register")).check(matches(isDisplayed()));
        onView(withId(R.id.tvRegisterLink)).perform(click());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Register");
    }

    @org.junit.Ignore
    public void process() {

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Login");

        fail();

        success();

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

    }

    private void success() {
        onView(withId(R.id.etUsername)).perform(typeText("apple")).check(matches(withText("apple")));
        onView(withId(R.id.etPassword)).perform(typeText("Password123")).check(matches(withText("Password123")));
        onView(withId(R.id.btnLogin)).perform(click());
    }

    private void fail() {
        //region case 1 : no username and no password
        onView(withId(R.id.btnLogin)).perform(click());
//        come back in future bcos dun know how to check
//        onView(withText(R.string.strPlsWait)).inRoot(isDialog()).check(matches(isDisplayed()));

        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInputUsername))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInputPassword))));
        resetAllInput();
        //endregion

        //region case 2 : with username and no password
        onView(withId(R.id.etUsername)).perform(typeText("apple")).check(matches(withText("apple")));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInputPassword))));
        resetAllInput();
        //endregion

        //region case 3 : with username and wrong password
        onView(withId(R.id.etUsername)).perform(typeText("apple")).check(matches(withText("apple")));
        onView(withId(R.id.etPassword)).perform(typeText(mWrongPassword));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.strIncorrectUserDetails)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        resetAllInput();
        //endregion
    }

    @org.junit.Ignore
    public void multipleLogin() {
        for (int i = 0; i < 10; i++) {
            AutoAction.autoLogout();
            //make sure password is correct
            AutoAction.autoLogin("apple", "Password123");
        }
    }

    private void resetAllInput() {
        onView(withId(R.id.etUsername)).perform(clearText());
        onView(withId(R.id.etPassword)).perform(clearText());
    }

}
