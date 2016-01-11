package com.msi.nearbyplacefinder.DataModel;

import java.io.Serializable;

/**
 * Created by MSi on 10/27/2015.
 */
public class UserInfoDataModel implements Serializable {

    private double userLatitude;
    private double userLongitude;


    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }
}
