package com.msi.nearbyplacefinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.msi.nearbyplacefinder.ApplicationHelper.GpsTracker;
import com.msi.nearbyplacefinder.ApplicationHelper.InternetChecker;
import com.msi.nearbyplacefinder.ApplicationHelper.LocationPublisher;
import com.msi.nearbyplacefinder.DataModel.UserInfoDataModel;


import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzid.runOnUiThread;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String Shared_Name = "DistanceRaius";
    private static final int Radius = 2000;

    ProgressDialog dialog;
    private LocationPublisher locationPublisher;

    private LatLng UserPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting current GPS location ...");
        dialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("DistanceRaius", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getInt("radius", 0) == 0) {
            editor.putInt("radius", 2000);
        }
        editor.commit();



        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.MainLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationPublisher = new LocationPublisher(MainActivity.this);

                if(locationPublisher.LocationProvider().getUserLatitude() != 0.0 && locationPublisher.LocationProvider().getUserLongitude()!= 0.0)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                });
                                Thread.sleep(900);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserPosition = new LatLng(locationPublisher.LocationProvider().getUserLatitude(),locationPublisher.LocationProvider().getUserLongitude());

                                        Intent intent = new Intent(MainActivity.this,PlaceTypeActivity.class);
                                        startActivity(intent);
                                    }
                                });

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.hide();
                                    }
                                });
                            }
                        }
                    }).start();
                }
                else
                {
                   // locationPublisher .LocationProvider();

                }
            }
        });

    }
}
