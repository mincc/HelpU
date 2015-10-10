package com.example.chungmin.helpu.ui_test.common;

import android.app.Activity;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CloseKeyboardAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.internal.util.Checks;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chungmin.helpu.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.pressMenuKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by Chung Min on 9/13/2015.
 */
public class ActivityUtils {
    public static String mTag = "ActivityUtils";

    public static Activity getActivityInstance() {
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

    public static ViewAction closeSoftKeyboard() {
        return new ViewAction() {
            /**
             * The delay time to allow the soft keyboard to dismiss.
             */
            private static final long KEYBOARD_DISMISSAL_DELAY_MILLIS = 500L;

            /**
             * The real {@link CloseKeyboardAction} instance.
             */
            private final ViewAction mCloseSoftKeyboard = new CloseKeyboardAction();

            @Override
            public Matcher<View> getConstraints() {
                return mCloseSoftKeyboard.getConstraints();
            }

            @Override
            public String getDescription() {
                return mCloseSoftKeyboard.getDescription();
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                mCloseSoftKeyboard.perform(uiController, view);
                uiController.loopMainThreadForAtLeast(KEYBOARD_DISMISSAL_DELAY_MILLIS);
            }
        };
    }

    public static Matcher<? super View> hasErrorText(String expectedError) {
        return new ErrorTextMatcher(expectedError);
    }

    public static class ErrorTextMatcher extends TypeSafeMatcher<View> {
        private final String expectedError;

        private ErrorTextMatcher(String expectedError) {
            this.expectedError = Checks.checkNotNull(expectedError);
        }

        @Override
        public boolean matchesSafely(View view) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                return expectedError.equals(editText.getError());
            } else if (view instanceof TextView) {
                TextView textView = (TextView) view;
                return expectedError.equals(textView.getError());
            } else {
                return false;
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with error: " + expectedError);
        }
    }

    public static void fromActivityToActivity(Class<?> from, Class<?> to) {
        fromActivityToActivity(from, to, "");
    }

    /*
        only one step to reach other acticity
     */
    public static void fromActivityToActivity(Class<?> from, Class<?> to, String shareActivity) {
        switch (from.getSimpleName()) {
            case "MainActivity":
                //region MainActivity
                switch (to.getSimpleName()) {
                    case "Work":
                        onView(ViewMatchers.withId(R.id.btnWork)).perform(click());
                        break;
                    case "Login":
                        onView(withId(R.id.btnLogout)).perform(click());
                        break;
                    case "UserProfileSetting":
                        onView(withId(R.id.txUsername)).perform(click());
                        break;
                    case "CustomerRequestList":
                        onView(withId(R.id.tvTotalCountHire)).perform(click());
                        break;
                    case "ServiceProviderList":
                        onView(withId(R.id.tvTotalCountWork)).perform(click());
                        break;
                    case "Hire":
                        onView(withId(R.id.btnHire)).perform(click());
                        break;
                    case "CustomerRequestJobList":
                        if (shareActivity == "JobOffer") {
                            onView(withId(R.id.tvTotalCountJobOffer)).perform(click());
                        } else if (shareActivity == "JobDone") {
                            onView(withId(R.id.tvTotalCountJobDone)).perform(click());
                        }
                        break;
                    case "ChangePasswordActivity":
                        onView(isRoot()).perform(pressMenuKey());
                        onView(withText(R.string.action_change_password)).check(matches(isDisplayed())).perform(click());
                        break;
                    case "CustomerCareActivity":
                        onView(isRoot()).perform(pressMenuKey());
                        onView(withText(R.string.strCustomerCare)).check(matches(isDisplayed())).perform(click());
                        break;
                    case "AboutUs":
                        onView(isRoot()).perform(pressMenuKey());
                        onView(withText(R.string.action_about_us)).check(matches(isDisplayed())).perform(click());
                        break;
                    case "ContactUs":
                        onView(isRoot()).perform(pressMenuKey());
                        onView(withText(R.string.action_contact_us)).check(matches(isDisplayed())).perform(click());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "Login":
                //region Login
                switch (to.getSimpleName()) {
                    case "Register":
                        onView(withId(R.id.tvRegisterLink)).perform(click());
                        break;
                    case "AccountRecoveryActivity":
                        onView(withId(R.id.tvForgetPassword)).perform(click());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "ServiceProviderList":
                //region ServiceProviderList
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "UserProfileSetting":
                //region UserProfileSetting
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "CustomerRequestList":
                //region CustomerRequestList
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "Hire":
                //region Hire
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(ActivityUtils.closeSoftKeyboard());
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "Work":
                //region Work
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "CustomerRequestJobList":
                //region CustomerRequestJobList
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "ServiceProviderListByServiceID":
                //region ServiceProviderListByServiceID
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "ProjectMessages":
                //region ProjectMessages
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "ChangePasswordActivity":
                //region ChangePassword
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "CustomerCareActivity":
                //region Sample
                switch (to.getSimpleName()) {
                    case "MainActivity":
                        onView(isRoot()).perform(pressBack());
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            case "Sample":
                //region Sample
                switch (to.getSimpleName()) {
                    case "":
                        break;
                    default:
                        String message = "From : " + from.getSimpleName() +
                                " To : " + to.getSimpleName() +
                                " : Undefined Class To Action";
                        Log.d(mTag, message);
                }
                //endregion
                break;
            default:
                String message = "From : " + from.getSimpleName() +
                        " To : " + to.getSimpleName() +
                        " : Undefined Class From Action";
                Log.d(mTag, message);
        }

    }
}
