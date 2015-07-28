package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/19/2015.
 */
public class User {

    String name, username, password;
    int userId;
    int age;

    public User(int userId, String name, int age, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this(-1, "", -1, username, password);
    }

    public User(int userId, String name){
        this(userId, name, -1, "", "");
    }
}