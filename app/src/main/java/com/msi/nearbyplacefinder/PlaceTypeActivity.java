package com.msi.nearbyplacefinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.msi.nearbyplacefinder.Adapters.PlaceTypeListAdapter;
import com.msi.nearbyplacefinder.ApplicationHelper.LocationPublisher;

/**
 * Created by MSi on 10/28/2015.
 */
public class PlaceTypeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView settings,about;

    private String []PlaceType = new String[]{"Atm booth","Bank","Bus station","Gas station",
            "Grocery or Supermarket","Hospital","Mosque", "Museum",
            "Police station","Post office","Restaurent","Subway station","Train station"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placetype_activity);

        toolbar = (Toolbar) findViewById(R.id.toobar_withmenu);
        toolbar.setTitle("Places near me");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settings = (ImageView) findViewById(R.id.settings);
        about = (ImageView) findViewById(R.id.about);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(PlaceTypeActivity.this,AppSettings.class);
                startActivity(settingsIntent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(PlaceTypeActivity.this,AppAbout.class);
                startActivity(aboutIntent);
            }
        });



        final ListView listView = (ListView) findViewById(R.id.placelist);
        PlaceTypeListAdapter placeTypeListAdapter = new PlaceTypeListAdapter(PlaceTypeActivity.this,PlaceType);
        listView.setAdapter(placeTypeListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               LocationPublisher locationPublisher = new LocationPublisher(PlaceTypeActivity.this);
                if(locationPublisher.LocationProvider().getUserLatitude() != 0.0 && locationPublisher.LocationProvider().getUserLongitude()!= 0.0)
                {
                    Intent intent = new Intent(PlaceTypeActivity.this, PlaceActivity.class);
                    intent.putExtra("PlaceType",PlaceType[position]);
                    intent.putExtra("PlaceIndex",position);
                    intent.putExtra("lat",locationPublisher.LocationProvider().getUserLatitude());
                    intent.putExtra("lng",locationPublisher.LocationProvider().getUserLongitude());
                    startActivity(intent);
                }
                else
                {
                 //   locationPublisher .LocationProvider();
                }
            }
        });

    }

//        @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.about:
//                Intent aboutIntent = new Intent(PlaceTypeActivity.this,AppAbout.class);
//                startActivity(aboutIntent);
//                break;
//            case R.id.settings:
//                Intent settingsIntent = new Intent(PlaceTypeActivity.this,AppSettings.class);
//                startActivity(settingsIntent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
