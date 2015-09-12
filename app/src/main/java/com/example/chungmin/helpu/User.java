package com.example.chungmin.helpu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class User implements Parcelable {

    private String name, username, password, userContact, userEmail;
    private int userId;

    public User(int userId, String name, String username, String password, String userContact, String userEmail) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.userContact = userContact;
        this.userEmail = userEmail;
    }

    public User(String username, String password) {
        this(-1, "", username, password, "", "");
    }

    public User(int userId, String name){
        this(userId, name, "", "", "", "");
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return name;
    }
    public void setUserName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserContact() {
        return userContact;
    }
    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.userContact);
        dest.writeString(this.userEmail);
        dest.writeInt(this.userId);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.userContact = in.readString();
        this.userEmail = in.readString();
        this.userId = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}