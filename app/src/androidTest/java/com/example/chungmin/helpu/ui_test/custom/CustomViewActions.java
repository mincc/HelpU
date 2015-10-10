package com.example.chungmin.helpu.ui_test.custom;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RatingBar;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Chung Min on 9/17/2015.
 */
public class CustomViewActions {

    public static ViewAction scrollToMatch(final Matcher<View> viewMatcher) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), is(instanceOf(AdapterView.class)));
            }

            @Override
            public String getDescription() {
                return "scroll to item";
            }

            @Override
            public void perform(UiController uiController, View view) {
                AdapterView adapterView = (AdapterView) view;
                final int count = adapterView.getCount();
                for (int i = 0; i < count; ++i) {
                    adapterView.setSelection(i);
                    uiController.loopMainThreadUntilIdle();
                    final int childCount = adapterView.getChildCount();
                    for (int at = 0; at < childCount; ++at) {
                        final View childAt = checkNotNull(adapterView.getChildAt(at));
                        final Matcher<View> displayedChild = allOf(viewMatcher, isDisplayingAtLeast(90));
                        if (ViewMatchers.hasDescendant(displayedChild).matches(childAt)) {
                            return;
                        }
                    }
                }
                throw new PerformException.Builder()
                        .withActionDescription("could not find item")
                        .withViewDescription(HumanReadables.describe(view))
                        .build();

            }
        };
    }

    public static ViewAction setRating(final double mRandomRatingValue) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                Matcher<View> isRatingBarConstraint = isAssignableFrom(RatingBar.class);
                return isRatingBarConstraint;
            }

            @Override
            public String getDescription() {

                return "Custom view action to set rating.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RatingBar ratingBar = (RatingBar) view;
                ratingBar.setRating((float) mRandomRatingValue);
            }
        };
    }
}
