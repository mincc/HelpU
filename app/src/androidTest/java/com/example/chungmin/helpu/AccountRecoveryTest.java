package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.AccountRecoveryActivity;
import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.common.ActivityUtils;
import com.example.chungmin.helpu.common.AutoAction;


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
 * Created by Chung Min on 9/22/2015.
 */
public class AccountRecoveryTest {
    Activity mActivity = null;

    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);
    private String mInvalidEmailAddress;
    private String mEmailAddressNotExist;

    @org.junit.Ignore
    public void isDisplayObjectExist() {
        AutoAction.goToActivity(Login.class, AccountRecoveryActivity.class);

        //TextView
        onView(withText(R.string.strSendNewPassword)).check(matches(isDisplayed()));
        onView(withText(R.string.strEmailAddress)).check(matches(isDisplayed()));

        //EditText
        onView(withId(R.id.etEmailAddress)).check(matches(isDisplayed()));

        //Button
        onView(withId(R.id.btnSendEmail)).check(matches(isDisplayed()));
    }


    @org.junit.Ignore
    public void process() {
        mActivity = main.getActivity();
        mInvalidEmailAddress = ValueGenerator.getRandomString((short) 20);
        mEmailAddressNotExist = "11111@ggmail.com";

        AutoAction.goToActivity(Login.class, AccountRecoveryActivity.class);

        fail();

        success();

        //manually check the email and get the new reset password
    }

    private void success() {
        String email = "bengsnail2002@yahoo.com";
        onView(withId(R.id.etEmailAddress)).perform(typeText(email));
        onView(withId(R.id.btnSendEmail)).perform(click());
        onView(withText(R.string.strPwdEmailSend)).check(matches(isDisplayed()));
    }

    private void fail() {
        //region case 1 : click "Send Email"
        onView(withId(R.id.btnSendEmail)).perform(click());
        onView(withId(R.id.etEmailAddress))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strEmailEmpty))));
        //endregion

        //region case 2 : invalid email address
        onView(withId(R.id.etEmailAddress)).perform(typeText(mInvalidEmailAddress));
        onView(withId(R.id.btnSendEmail)).perform(click());
        onView(withId(R.id.etEmailAddress))
                .check(matches(ActivityUtils.hasErrorText(mActivity.getString(R.string.strEmailInvalid))));
        resetAllInput();
        //endregion

        //region case 3 : email not exists
        onView(withId(R.id.etEmailAddress)).perform(typeText(mEmailAddressNotExist));
        onView(withId(R.id.btnSendEmail)).perform(click());
        onView(withText(R.string.strEmailNotExist)).check(matches(isDisplayed()));
        resetAllInput();
        //endregion

    }

    private void resetAllInput() {
        onView(withId(R.id.etEmailAddress)).perform(clearText());
    }
}
