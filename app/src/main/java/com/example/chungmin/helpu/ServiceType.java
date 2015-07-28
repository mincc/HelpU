package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/27/2015.
 */
public enum ServiceType {
    Create("Please select...", 0),
    Contractor("Contractor", 1),
    Plumber("Plumber", 2),
    Handyman("Handyman", 3),
    InteriorDesigner("Interior Designer", 4),
    CleaningService("Cleaning Service", 5),
    MoveHouse("Move House", 6),
    Photographer("Photographer", 7),
    Videographer("Videographer", 8),
    HairAndMakeup("Hair and Makeup", 9),
    WeddingPlanner("Wedding Planner", 10);

    private String stringValue;
    private int intValue;
    private ServiceType(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getId(){
        return intValue;
    }

//    convert From Int To Enum
//    ProjectStatus.values()[productId];
//
//    convert From
}
