package com.example.chungmin.helpu.common;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.CustomerRequestList;
import com.example.chungmin.helpu.activities.Hire;
import com.example.chungmin.helpu.activities.Login;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ProjectMessages;
import com.example.chungmin.helpu.activities.Register;
import com.example.chungmin.helpu.activities.ServiceProviderList;
import com.example.chungmin.helpu.activities.ServiceProviderListByServiceID;
import com.example.chungmin.helpu.activities.Work;
import com.example.chungmin.helpu.custom.CustomMatchers;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.User;

import java.util.ArrayList;
import java.util.List;

import HelpUTestUtilities.ValueGenerator;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Chung Min on 9/15/2015.
 */
public class AutoAction {
    static Activity mActivity = null;
    static String mClassName = "";
    static Class mClass;

    public static void autoLogin(String username, String password) {
        onView(ViewMatchers.withId(R.id.etUsername)).perform(typeText(username));
        onView(withId(R.id.etPassword)).perform(typeText(password));
        onView(withId(R.id.btnLogin)).perform(click());
    }

    public static User userLogin(boolean isExistingUser) {
        User user = new User();
        String username;
        String password;

        String[][] randomExistingUser = {
                {"appleHilton", "dACZQha"},
                {"Blondell", "CZQbdUL"},
                {"Shonta", "axoL6sh"},
                {"Long", "wRGypMC"},
                {"Tarra", "8ZXNkaS"},
                {"Emily", "epf6sa!"},
                {"Man", "peQG02J"},
                {"Krysten", "Krysten"},
                {"Vertie", "D0RmwTK"},
                {"Donte", "H46siFY"},
                {"Florencio", "Le6TVg8"},
                {"Kelley", "A66MF9u"},
                {"Lashawnda", "7tj9!tQ"}};

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        if (isExistingUser) {
            int randomInt = ValueGenerator.getRandomInt(0, 12);
            username = randomExistingUser[randomInt][0];
            password = randomExistingUser[randomInt][1];

            if (mClassName.equals("MainActivity")) {
                ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);
            }

            user.setUsername(username);
            user.setPassword(password);
        } else {
            if (mClassName.equals("MainActivity")) {
                goToActivity(MainActivity.class, Register.class);
            } else if (mClassName.equals("Login")) {
                ActivityUtils.fromActivityToActivity(Login.class, Register.class);
            }
            user = registerNewUser();
            username = user.getUsername();
            password = user.getPassword();
        }

        mActivity = ActivityUtils.getActivityInstance();
        mClassName = mActivity.getClass().getSimpleName();
        if (mClassName.equals("Login")) {
            AutoAction.autoLogin(username, password);
        }

        return user;
    }

    public static User registerNewUser() {
        String mRandomUserName = ValueGenerator.getRandomUserName();
        String mRandomUsername = ValueGenerator.getRandomUsername();
        String mRandomPassword = ValueGenerator.getRandomString((short) 7);
        String mRandomRetypePassword = mRandomPassword;
        String mRandomContact = ValueGenerator.getRandomMobileNumber();
        String mRandomEmail = ValueGenerator.getRandomEmailAddress();

        User user = new User(0, mRandomUserName, mRandomUsername, mRandomPassword, mRandomContact, mRandomEmail);

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

        return user;
    }

    public static void goToActivity(Class<?> from, Class<?> to) {
        switch (from.getSimpleName()) {
            case "MainActivity":
                //region MainActivity
                switch (to.getSimpleName()) {
                    case "Register":
                        ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);
                        ActivityUtils.fromActivityToActivity(Login.class, Register.class);
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            case "ServiceProviderListByServiceID":
                //region ServiceProviderListByServiceID
                switch (to.getSimpleName()) {
                    case "Hire":
                        ActivityUtils.fromActivityToActivity(ServiceProviderListByServiceID.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, Hire.class);
                        break;
                    case "CustomerRequestList":
                        ActivityUtils.fromActivityToActivity(ServiceProviderListByServiceID.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestList.class);
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            case "Hire":
                //region Hire
                switch (to.getSimpleName()) {
                    case "CustomerRequestList":
                        ActivityUtils.fromActivityToActivity(Hire.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, CustomerRequestList.class);
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            case "ServiceProviderList":
                //region ServiceProviderList
                switch (to.getSimpleName()) {
                    case "Work":
                        ActivityUtils.fromActivityToActivity(ServiceProviderList.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, Work.class);
                        break;
                    case "Login":
                        ActivityUtils.fromActivityToActivity(ServiceProviderList.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            case "Work":
                //region Work
                switch (to.getSimpleName()) {
                    case "ServiceProviderList":
                        ActivityUtils.fromActivityToActivity(Work.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, ServiceProviderList.class);
                        break;
                    case "Login":
                        ActivityUtils.fromActivityToActivity(Work.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            case "ProjectMessages":
                //region ProjectMessages
                switch (to.getSimpleName()) {
                    case "Login":
                        ActivityUtils.fromActivityToActivity(ProjectMessages.class, MainActivity.class);
                        ActivityUtils.fromActivityToActivity(MainActivity.class, Login.class);
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            case "Sample":
                //region Sample
                switch (to.getSimpleName()) {
                    case "":
                        break;
                    default:
                        ActivityUtils.fromActivityToActivity(from, to);
                        break;
                }
                //endregion
                break;
            default:
                ActivityUtils.fromActivityToActivity(from, to);
                break;
        }
    }

    public static List<CustomerRequest> autoHireList() {
        List<CustomerRequest> customerRequestList = new ArrayList<CustomerRequest>();

        for (int i = 0; i < 3; i++) {
            CustomerRequest customerRequest = autoHire();
            customerRequestList.add(customerRequest);
            AutoAction.goToActivity(ServiceProviderListByServiceID.class, Hire.class);
        }

        return customerRequestList;
    }

    public static CustomerRequest autoHire() {
        return autoHire(true, 0);
    }

    public static CustomerRequest autoHire(boolean isRandomService, int serviceId) {
        int randomServiceId = 0;
        if (isRandomService) {
            randomServiceId = ValueGenerator.getRandomInt(1, 10);
        } else {
            randomServiceId = serviceId;
        }
        String mRandomDescription = ValueGenerator.getRandomComment();
        onView(withId(R.id.spHire)).perform(click());
        onData(CustomMatchers.withServiceId(randomServiceId))
                .perform(click());
        onView(withId(R.id.etDescription))
                .perform(typeText(mRandomDescription), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.btnOk)).perform(click());

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setServiceId(randomServiceId);
        customerRequest.setDescription(mRandomDescription);

        return customerRequest;
    }

    public static void autoLogout() {
        mActivity = ActivityUtils.getActivityInstance();
        mClass = mActivity.getClass();

        if (!mClass.equals(Login.class)) {
            AutoAction.goToActivity(mClass, Login.class);
        }
    }

    public static ServiceProvider autoWork() {
        return autoWork(true, 0);
    }

    public static ServiceProvider autoWork(boolean isRandomService, int serviceId) {

        int randomServiceId = 0;
        if (isRandomService) {
            randomServiceId = ValueGenerator.getRandomInt((short) 1, (short) 10);
        } else {
            randomServiceId = serviceId;
        }
        String randomContact = ValueGenerator.getRandomMobileNumber();
        String randomEmail = ValueGenerator.getRandomEmailAddress();

        onView(withId(R.id.spWork)).perform(click());
        onData(CustomMatchers.withServiceId(randomServiceId))
                .perform(click());
        onView(withId(R.id.etContact))
                .perform(typeText(randomContact), ActivityUtils.closeSoftKeyboard());
        onView(withId(R.id.etEmail))
                .perform(typeText(randomEmail), ActivityUtils.closeSoftKeyboard());

        onView(withId(R.id.btnRegister))
                .perform(click());

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setServiceId(randomServiceId);
        serviceProvider.setPhone(randomContact);
        serviceProvider.setEmail(randomEmail);

        return serviceProvider;
    }

    public static List<ServiceProvider> autoWorkList() {
        List<ServiceProvider> serviceProviderListList = new ArrayList<ServiceProvider>();

        for (int i = 0; i < 2; i++) {
            int serviceId = i + 1;
            ServiceProvider serviceProvider = autoWork(false, serviceId);
            serviceProviderListList.add(serviceProvider);
            AutoAction.goToActivity(ServiceProviderList.class, Work.class);
        }

        return serviceProviderListList;
    }
}
