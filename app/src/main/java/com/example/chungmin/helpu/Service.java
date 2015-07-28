package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/22/2015.
 */
public class Service {
    public int id = 0;
    public String name = "";

    public Service(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
