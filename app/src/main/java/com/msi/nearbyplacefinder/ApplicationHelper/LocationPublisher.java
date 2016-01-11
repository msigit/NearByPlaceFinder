package com.msi.nearbyplacefinder.ApplicationHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.msi.nearbyplacefinder.DataModel.JsonDataModel;
import com.msi.nearbyplacefinder.DataModel.UserInfoDataModel;
import com.msi.nearbyplacefinder.MainActivity;
import com.msi.nearbyplacefinder.PlaceActivity;

import static android.support.v4.app.ActivityCompat.startActivity;
import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by MSi on 10/28/2015.
 */
public class LocationPublisher {

    private Context context;

    private InternetChecker internetChecker;
    private GpsTracker gpsTracker;
    private UserInfoDataModel userInfoDataModel;


    public LocationPublisher(Context context) {
        this.context = context;
    }

    public UserInfoDataModel LocationProvider()
    {
        internetChecker = new InternetChecker(context);
        gpsTracker = new GpsTracker(context);
        userInfoDataModel = new UserInfoDataModel();


        if(!internetChecker.isInternetConnected())
        {
            internetChecker.showInternetAlert();
        }
        else
        {
            if(!gpsTracker.canGetLocation())
            {
                gpsTracker.showGPSAlert();
            }
            else
            {
                userInfoDataModel.setUserLatitude(gpsTracker.getLatitude());
                userInfoDataModel.setUserLongitude(gpsTracker.getLongitude());
            }
        }


//        if(internetChecker.isInternetConnected())
//        {
//            if(gpsTracker.canGetLocation())
//            {
//                userInfoDataModel.setUserLatitude(gpsTracker.getLatitude());
//                userInfoDataModel.setUserLongitude(gpsTracker.getLongitude());
//            }
//            else if(!gpsTracker.canGetLocation())
//            {
//                gpsTracker.showGPSAlert();
//            }
//        }
//        else if(!internetChecker.isInternetConnected())
//        {
//            internetChecker.showInternetAlert();
//        }
        return userInfoDataModel;
    }
}
