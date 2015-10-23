package com.example.chungmin.helpu.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chungmin.helpu.models.User;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("userId", user.getUserId());
        userLocalDatabaseEditor.putString("name", user.getUserName());
        userLocalDatabaseEditor.putString("username", user.getUsername());
        userLocalDatabaseEditor.putString("password", user.getPassword());
        userLocalDatabaseEditor.putString("userContact", user.getUserContact());
        userLocalDatabaseEditor.putString("userEmail", user.getUserEmail());
        userLocalDatabaseEditor.putInt("isAdmin", user.getIsAdmin());
        userLocalDatabaseEditor.putString("gcmRegId", user.getGcmRegId());
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        int userId = userLocalDatabase.getInt("userId", -1);
        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String userContact = userLocalDatabase.getString("userContact", "");
        String userEmail = userLocalDatabase.getString("userEmail", "");
        int isAdmin = userLocalDatabase.getInt("isAdmin", 0);
        String gcmRegId = userLocalDatabase.getString("gcmRegId", "");

        User user = new User(userId, name, username, password, userContact, userEmail, isAdmin, gcmRegId);
        return user;
    }
}

