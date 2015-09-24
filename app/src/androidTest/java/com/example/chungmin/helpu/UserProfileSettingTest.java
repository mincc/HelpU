package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.Register;
import com.example.chungmin.helpu.activities.UserProfileSetting;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;
import com.example.chungmin.helpu.models.User;

import junit.framework.Assert;

import org.junit.Rule;

import HelpUTestUtilities.ValueGenerator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/14/2015.
 */
public class UserProfileSettingTest {
    Activity mActivity = null;
    String mClassName = "";

    @Rule
    public final ActivityTestRule<UserProfileSetting> main = new ActivityTestRule<>(UserProfileSetting.class);
    private String mRandomContact;
    private String mRandomName;
    private String mRandomEmail;
    private User mUser = null;


    @org.junit.Ignore
    public void isDisplayObjectExist() {
        //TextView
        onView(withText(R.string.strUsername)).check(matches(isDisplayed()));
        onView(withText(R.string.strName)).check(matches(isDisplayed()));
        onView(withText(R.string.strContact)).check(matches(isDisplayed()));
        onView(withText(R.string.strEmail)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.etUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.etName)).check(matches(isDisplayed()));
        onView(withId(R.id.etUserContact)).check(matches(isDisplayed()));
        onView(withId(R.id.etUserEmail)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnSave)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void process() {
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "UserProfileSetting");

        //go to register activity from user profile setting
        ActivityUtils.fromActivityToActivity(UserProfileSetting.class, MainActivity.class);
        ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);
        ActivityUtils.fromActivityToActivity(Login.class, Register.class);

        //random register a user
        mUser = AutoAction.registerNewUser();
        AutoAction.autoLogin(mUser.getUsername(), mUser.getPassword());

        //go back to user profile setting
        ActivityUtils.fromActivityToActivity(MainActivity.class, UserProfileSetting.class);

        failUpdate();
        successfUpdate();
    }

    private void failUpdate() {
        //region case 1 : Name not fill
        onView(withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etName)).perform(click(), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        resetToOriginal();
        //endregion

        //region case 2 : Contact not fill
        onView(withId(R.id.etUserContact)).perform(clearText());
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etUserContact)).perform(click(), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        resetToOriginal();
        //endregion

        //region case 3 : Email not fill
        onView(withId(R.id.etUserEmail)).perform(clearText());
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etUserEmail)).perform(click(), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        resetToOriginal();
        //endregion

        //region case 3 : Email invalid
        mRandomEmail = ValueGenerator.getRandomString((short) 10);
        onView(withId(R.id.etUserEmail)).perform(clearText());
        onView(withId(R.id.etUserEmail)).perform(typeText(mRandomEmail));
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etUserEmail)).perform(click(), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailInvalid))));
        resetToOriginal();
        //endregion
    }

    private void successfUpdate() {
        mRandomName = ValueGenerator.getRandomUsername();
        mRandomContact = ValueGenerator.getRandomMobileNumber();
        mRandomEmail = ValueGenerator.getRandomEmailAddress();

        onView(withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.etName)).perform(typeText(mRandomName));
        onView(withId(R.id.etUserContact)).perform(clearText());
        onView(withId(R.id.etUserContact)).perform(typeText(mRandomContact));
        onView(withId(R.id.etUserEmail)).perform(clearText());
        onView(withId(R.id.etUserEmail)).perform(typeText(mRandomEmail));

        onView(withId(R.id.btnSave)).perform(click());

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");

    }

    private void resetToOriginal() {
        mRandomName = mUser.getUserName();
        mRandomContact = mUser.getUserContact();
        mRandomEmail = mUser.getUserEmail();

        onView(withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.etName)).perform(typeText(mRandomName));
        onView(withId(R.id.etUserContact)).perform(clearText());
        onView(withId(R.id.etUserContact)).perform(typeText(mRandomContact));
        onView(withId(R.id.etUserEmail)).perform(clearText());
        onView(withId(R.id.etUserEmail)).perform(typeText(mRandomEmail));

    }
}
