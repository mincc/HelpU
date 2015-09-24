package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.Register;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;

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
 * Created by Chung Min on 9/13/2015.
 */
public class RegisterTest {
    Activity mActivity = null;
    String mClassName = "";

    static String mRandomUserName;
    static String mRandomUsername;
    static String mRandomPassword;
    static String mRandomRetypePassword;
    static String mRandomContact;
    static String mRandomEmail;

    @Rule
    public final ActivityTestRule<Register> main = new ActivityTestRule<>(Register.class);

    @org.junit.Ignore
    public void isDisplayObjectExist() {
        //TextView
        onView(withText(R.string.strName)).check(matches(isDisplayed()));
        onView(withText(R.string.strUsername)).check(matches(isDisplayed()));
        onView(withText(R.string.strPassword)).check(matches(isDisplayed()));
        onView(withText(R.string.strRetypePassword)).check(matches(isDisplayed()));
        onView(withText(R.string.strContact)).check(matches(isDisplayed()));
        onView(withText(R.string.strEmail)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.etName)).check(matches(isDisplayed()));
        onView(withId(R.id.etUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.etRetypePassword)).check(matches(isDisplayed()));
        onView(withId(R.id.etUserContact)).check(matches(isDisplayed()));
        onView(withId(R.id.etUserEmail)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
    }

    @org.junit.Ignore
    public void process() {
        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Register");

        //fail to register
        failRegister();

        //random value generator
        mRandomUserName = ValueGenerator.getRandomUsername();
        mRandomUsername = ValueGenerator.getRandomFirstName();
        mRandomPassword = ValueGenerator.getRandomString((short) 7);
        mRandomRetypePassword = mRandomPassword;
        mRandomContact = ValueGenerator.getRandomMobileNumber();
        mRandomEmail = ValueGenerator.getRandomEmailAddress();

        //success to register
        resetToOriginal();
        successRegister();

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "Login");

        //Login by using new created account
        AutoAction.autoLogin(mRandomUsername, mRandomPassword);

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        Assert.assertEquals(mClassName, "MainActivity");
    }

    private void failRegister() {
        //region case 1 : no detail
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        //endregion

        //region case 2 : only have Name
        resetToOriginal();
        mRandomUserName = ValueGenerator.getRandomUsername();

        onView(withId(R.id.etName))
                .perform(typeText(mRandomUserName), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(withText(mRandomUserName)));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        //endregion

        //region case 3 : only have Username
        resetToOriginal();
        mRandomUsername = ValueGenerator.getRandomFirstName();

        onView(withId(R.id.etUsername))
                .perform(typeText(mRandomUsername), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(withText(mRandomUsername)));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        //endregion

        //region case 4 : only have Password with wrong retype password
        resetToOriginal();
        mRandomPassword = ValueGenerator.getRandomString((short) 7);

        onView(withId(R.id.etPassword))
                .perform(typeText(mRandomPassword), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(withText(mRandomPassword)));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        onView(withId(R.id.etRetypePassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordMismatch))));
        //endregion

        //region case 5 : only have Password with correct retype password
        resetToOriginal();
        mRandomPassword = ValueGenerator.getRandomString((short) 7);

        onView(withId(R.id.etPassword))
                .perform(typeText(mRandomPassword), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etRetypePassword))
                .perform(typeText(mRandomPassword), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(withText(mRandomPassword)));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        onView(withId(R.id.etRetypePassword))
                .check(matches(withText(mRandomPassword)));
        //endregion

        //region case 6 : only have Contact
        resetToOriginal();
        mRandomContact = ValueGenerator.getRandomMobileNumber();

        onView(withId(R.id.etUserContact))
                .perform(typeText(mRandomContact), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(withText(mRandomContact)));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        //endregion

        //region case 7 : only have Email
        resetToOriginal();
        mRandomEmail = ValueGenerator.getRandomEmailAddress();

        onView(withId(R.id.etUserEmail))
                .perform(typeText(mRandomEmail), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(withText(mRandomEmail)));
        //endregion

        //region case 8 : Invalid email
        resetToOriginal();
        mRandomEmail = ValueGenerator.getRandomString((short) 10, (short) 15);

        onView(withId(R.id.etUserEmail))
                .perform(typeText(mRandomEmail), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailInvalid))));
        //endregion

        //region case 9 : Password too short
        resetToOriginal();
        mRandomPassword = ValueGenerator.getRandomString((short) 5);

        onView(withId(R.id.etPassword))
                .perform(typeText(mRandomPassword), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameEmpty))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordTooShort))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        onView(withId(R.id.etRetypePassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordMismatch))));
        //endregion

        //region case 10 : username already exist
        resetToOriginal();
        mRandomUsername = "apple";

        onView(withId(R.id.etUsername))
                .perform(typeText(mRandomUsername), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etName))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strNameEmpty))));
        onView(withId(R.id.etUsername)) //make sure username apple already exist in DB
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUsernameAlreadyExists))));
        onView(withId(R.id.etPassword))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strPasswordEmpty))));
        onView(withId(R.id.etUserContact))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserContactEmpty))));
        onView(withId(R.id.etUserEmail))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strUserEmailEmpty))));
        //endregion
    }

    private void successRegister() {
        createRandomUser();
    }

    private void createRandomUser() {
        onView(withId(R.id.etName))
                .perform(typeText(mRandomUserName), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etUsername))
                .perform(typeText(mRandomUsername), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etPassword))
                .perform(typeText(mRandomPassword), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etUserContact))
                .perform(typeText(mRandomContact), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etUserEmail))
                .perform(typeText(mRandomEmail), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etRetypePassword))
                .perform(typeText(mRandomRetypePassword), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
    }

    private void resetToOriginal() {
        onView(withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.etUsername)).perform(clearText());
        onView(withId(R.id.etPassword)).perform(clearText());
        onView(withId(R.id.etUserContact)).perform(clearText());
        onView(withId(R.id.etUserEmail)).perform(clearText());
        onView(withId(R.id.etRetypePassword)).perform(clearText());
    }

}
