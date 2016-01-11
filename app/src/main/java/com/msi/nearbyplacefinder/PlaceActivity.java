package com.msi.nearbyplacefinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.msi.nearbyplacefinder.Adapters.PlaceListAdapter;
import com.msi.nearbyplacefinder.ApplicationHelper.JsonDataParser;
import com.msi.nearbyplacefinder.ApplicationHelper.LocationPublisher;
import com.msi.nearbyplacefinder.ApplicationHelper.StaticService;
import com.msi.nearbyplacefinder.DataModel.JsonDataModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MSi on 10/28/2015.
 */
public class PlaceActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressDialog dialog;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private TextView no_location;

    private PlaceListAdapter  placeListAdapter;

    private long radius ;
    private String PlaceType;
    private int PlaceIndex;
    private  ArrayList<JsonDataModel> dataModelArrayList;
    private LatLng UserPosition;

    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_activity);

        pref = getSharedPreferences("DistanceRaius", 0);
        radius = pref.getInt("radius",0);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        listView = (ListView) findViewById(R.id.placelist);

        final Bundle bundle = getIntent().getExtras();
        PlaceType = bundle.getString("PlaceType");
        PlaceIndex = bundle.getInt("PlaceIndex");
        UserPosition = new LatLng(bundle.getDouble("lat"),bundle.getDouble("lng"));

        linearLayout = (LinearLayout) findViewById(R.id.no_location_found);
        no_location = (TextView) findViewById(R.id.no_location);
        toolbar = (Toolbar) findViewById(R.id.toobar_withoutmenu);

        if(radius <= 1000) toolbar.setTitle(PlaceType + "s near 1000 meters");
        else toolbar.setTitle(PlaceType + "s near " + radius / 1000 + " Km");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        PlaceDataParser();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LocationPublisher locationPublisher = new LocationPublisher(PlaceActivity.this);
                if(locationPublisher.LocationProvider().getUserLatitude() != 0.0 && locationPublisher.LocationProvider().getUserLongitude()!= 0.0)
                {
                    Intent intent = new Intent(PlaceActivity.this, PlaceDetails.class);
                    intent.putExtra("PlaceId", dataModelArrayList.get(position).getPlaceId());
                    intent.putExtra("PlaceName", dataModelArrayList.get(position).getPlaceName());
                    intent.putExtra("PlaceAddress", dataModelArrayList.get(position).getPlaceAddress());
                    intent.putExtra("PlaceLat", dataModelArrayList.get(position).getPlaceLatitude());
                    intent.putExtra("PlaceLng", dataModelArrayList.get(position).getPlaceLongitude());
                    intent.putExtra("lat", locationPublisher.LocationProvider().getUserLatitude());
                    intent.putExtra("lng", locationPublisher.LocationProvider().getUserLongitude());

                    startActivity(intent);
                }
                else
                {
                   // locationPublisher .LocationProvider();
                }
            }
        });
    }


    public void PlaceDataParser()
    {
        dialog.show();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location="+UserPosition.latitude+","+UserPosition.longitude+"&radius="+radius + "&types=" +
                StaticService.PlaceType(PlaceType)+"&key=AIzaSyCLltShL1vvXLQDOjTOuQlkhzwuPQEMuqA";

        RequestQueue requestQueue = Volley.newRequestQueue(PlaceActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                         dataModelArrayList = new JsonDataParser().jsonDataModelArray(response);

                        if(dataModelArrayList.size() == 0)
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            if(radius <= 1000) no_location.setText("No "+PlaceType+" found near 1000 meters");
                            else no_location.setText("No "+PlaceType+" found near "+ radius / 1000 + " Km");
                        }
                        else
                        {
                            linearLayout.setVisibility(View.GONE);
                            placeListAdapter = new PlaceListAdapter(PlaceActivity.this, dataModelArrayList, PlaceIndex,new LatLng(UserPosition.latitude, UserPosition.longitude));
                            listView.setAdapter(placeListAdapter);
                        }
                        dialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Parse Error",error.getMessage());
                        dialog.hide();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}