package com.example.chungmin.helpu;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chung Min on 9/3/2015.
 */
public class TestEspressoTestCaseTest {
    @Rule
    public final ActivityTestRule<TestEspressoTestCase> main = new ActivityTestRule<>(TestEspressoTestCase.class);

    @org.junit.Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withText("Hello")).check(ViewAssertions.matches(isDisplayed()));
    }
}
