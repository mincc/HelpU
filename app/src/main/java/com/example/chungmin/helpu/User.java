package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class User {

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
}