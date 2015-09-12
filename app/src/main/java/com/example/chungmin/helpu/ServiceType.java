package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/27/2015.
 */
public enum ServiceType {
    Create(Globals.getAppContext().getString(R.string.strPlsSlct), 0),
    Contractor(Globals.getAppContext().getString(R.string.strContractor), 1),
    Plumber(Globals.getAppContext().getString(R.string.strPlumber), 2),
    Handyman(Globals.getAppContext().getString(R.string.strHandyman), 3),
    InteriorDesigner(Globals.getAppContext().getString(R.string.strInteriorDesigner), 4),
    CleaningService(Globals.getAppContext().getString(R.string.strCleaningService), 5),
    MoveHouse(Globals.getAppContext().getString(R.string.strMoveHouse), 6),
    Photographer(Globals.getAppContext().getString(R.string.strPhotographer), 7),
    Videographer(Globals.getAppContext().getString(R.string.strVideographer), 8),
    HairAndMakeup(Globals.getAppContext().getString(R.string.strHairAndMakeup), 9),
    WeddingPlanner(Globals.getAppContext().getString(R.string.strWeddingPlanner), 10);

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
//    ServiceType.values()[productId];
//
//    convert From
}
