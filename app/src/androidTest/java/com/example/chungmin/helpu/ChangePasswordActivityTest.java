package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.ChangePasswordActivity;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;

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
 * Created by Chung Min on 9/22/2015.
 */
public class ChangePasswordActivityTest {
    Activity mActivity = null;

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
    private String mCurrentPassword;
    private String mNewPassword;
    private String mMismatchNewPassword;
    private String mWrongCurrentPassword;
    private String mShortPassword;
    private String mClassName = "";


    @org.junit.Ignore
    public void isDisplayObjectExist() {
        AutoAction.goToActivity(MainActivity.class, ChangePasswordActivity.class);

        //EditText
        onView(withId(R.id.etCurrentPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.etNewPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.etRetypeNewPassword)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnCancel)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSave)).check(matches(isDisplayed()));

        AutoAction.goToActivity(ChangePasswordActivity.class, MainActivity.class);
    }

    @org.junit.Ignore
    public void changePassword() {
        mActivity = main.getActivity();

        //Please confirm the current password before run the test
        //due to every time run the test will change the original password
        mCurrentPassword = "Password123";
        mNewPassword = "Password1234";

        mWrongCurrentPassword = "1234567";
        mMismatchNewPassword = "123Password";
        mShortPassword = "12345";

        AutoAction.autoLogout();
        AutoAction.autoLogin("Krysten59", mCurrentPassword);
        AutoAction.goToActivity(MainActivity.class, ChangePasswordActivity.class);

        fail();

        success();

        loginWithNewPassword();
    }

    private void loginWithNewPassword() {
        AutoAction.autoLogout();
        AutoAction.autoLogin("Krysten59", mNewPassword);
    }

    private void success() {
        onView(withId(R.id.etCurrentPassword)).perform(typeText(mCurrentPassword));
        onView(withId(R.id.etNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.etRetypeNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.btnSave)).perform(click());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");
    }

    private void fail() {
        //region case 1 : no fill
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etCurrentPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInvalidPassword))));
        onView(withId(R.id.etNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        resetAllInput();
        //endregion

        //region case 2 : fill only current password
        onView(withId(R.id.etCurrentPassword)).perform(typeText(mCurrentPassword));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        resetAllInput();
        //endregion

        //region case 3 : fill only new password
        onView(withId(R.id.etNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etCurrentPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInvalidPassword))));
        onView(withId(R.id.etRetypeNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordMismatch))));
        resetAllInput();
        //endregion

        //region case 4 : fill only retype new password
        onView(withId(R.id.etRetypeNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etCurrentPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInvalidPassword))));
        onView(withId(R.id.etNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etRetypeNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordMismatch))));
        resetAllInput();
        //endregion

        //region case 5 : fill with wrong current password
        onView(withId(R.id.etCurrentPassword)).perform(typeText(mWrongCurrentPassword));
        onView(withId(R.id.etNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.etRetypeNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etCurrentPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strInvalidPassword))));
        resetAllInput();
        //endregion

        //region case 6 : fill with unmatch new password
        onView(withId(R.id.etCurrentPassword)).perform(typeText(mCurrentPassword));
        onView(withId(R.id.etNewPassword)).perform(typeText(mNewPassword));
        onView(withId(R.id.etRetypeNewPassword)).perform(typeText(mMismatchNewPassword));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etRetypeNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordMismatch))));
        resetAllInput();
        //endregion

        //region case 7 : password too short
        onView(withId(R.id.etCurrentPassword)).perform(typeText(mCurrentPassword));
        onView(withId(R.id.etNewPassword)).perform(typeText(mShortPassword));
        onView(withId(R.id.etRetypeNewPassword)).perform(typeText(mShortPassword));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etNewPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordTooShort))));
        resetAllInput();
        //endregion

        //region case 8 : cancel
        onView(withId(R.id.btnCancel)).perform(click());
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");
        AutoAction.goToActivity(MainActivity.class, ChangePasswordActivity.class);
        //endregion
    }

    private void resetAllInput() {
        onView(withId(R.id.etCurrentPassword)).perform(clearText());
        onView(withId(R.id.etNewPassword)).perform(clearText());
        onView(withId(R.id.etRetypeNewPassword)).perform(clearText());
    }
}
