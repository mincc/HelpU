package com.example.chungmin.helpu.models;

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

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
