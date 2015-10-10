package com.example.chungmin.helpu.ui_test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.models.User;

import org.junit.Rule;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/21/2015.
 */
public class ServiceProviderServerRequestTest {
    Activity mActivity = null;
    User mUser = null;


    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);


}
