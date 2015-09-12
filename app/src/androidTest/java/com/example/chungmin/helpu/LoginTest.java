package com.example.chungmin.helpu;

import android.app.Activity;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import org.junit.Rule;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by Chung Min on 9/3/2015.
 */
public class LoginTest {
    @Rule
    public final ActivityTestRule<Login> main = new ActivityTestRule<>(Login.class);

    @org.junit.Test
    public void isTextViewExist() {
        onView(withText("Username")).check(matches(isDisplayed()));
        onView(withText("Password")).check(matches(isDisplayed()));
        onView(withText("Register")).check(matches(isDisplayed()));
    }

    @org.junit.Test
    public void isEditTextExist() {
        onView(withId(R.id.etUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
    }

    @org.junit.Test
    public void isButtonExist() {
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    @org.junit.Test
    public void LoginProcess() {

        //case 1 : no username and no password
        onView(withId(R.id.btnLogin)).perform(click());
//        come back in future bcos dun know how to check
//        onView(withText(R.string.strPlsWait)).inRoot(isDialog()).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click()); //To click on dialogs button do this(button1 - OK, button2 - Cancel)
        onView(withText(R.string.strIncorrectUserDetails)).check(matches(isDisplayed()));

        //case 2 : with username and no password
        onView(withId(R.id.etUsername)).perform(typeText("apple"));
        onView(withId(R.id.etUsername)).check(matches(withText("apple")));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.strIncorrectUserDetails)).check(matches(isDisplayed()));

        //case 3 : with username and password
        onView(withId(R.id.etUsername)).perform(typeText("apple"));
        onView(withId(R.id.etUsername)).check(matches(withText("apple")));
        onView(withId(R.id.etPassword)).perform(typeText("Password123"));
        onView(withId(R.id.etPassword)).check(matches(withText("Password123")));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText(R.string.strIncorrectUserDetails)).check(matches(isDisplayed()));


    }

    public Activity getActivityInstance() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = (android.app.Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }
}
