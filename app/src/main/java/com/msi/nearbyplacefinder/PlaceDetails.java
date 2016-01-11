package com.msi.nearbyplacefinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.msi.nearbyplacefinder.ApplicationHelper.JsonDataParser;
import com.msi.nearbyplacefinder.ApplicationHelper.StaticService;
import com.msi.nearbyplacefinder.DataModel.JsonDataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by MSi on 10/31/2015.
 */
public class PlaceDetails extends AppCompatActivity {

    private String PlaceId;
    private String PlaceName;
    private String PlaceAddress;
    private LatLng PlaceLatLng;
    private LatLng UserLatLng;

    private ProgressDialog dialog;

    private double UserLat;
    private double UserLng;
    private double PlaceLat;
    private double PlaceLng;

    TextView placeName,placeAddress,placePhone,placeWebsite,placeDistance;
    private GoogleMap googleMap;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placedetails);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);

        final Bundle bundle = getIntent().getExtras();
        PlaceId = bundle.getString("PlaceId");
        PlaceName = bundle.getString("PlaceName");
        PlaceAddress = bundle.getString("PlaceAddress");
        PlaceLatLng = new LatLng(bundle.getDouble("PlaceLat"),bundle.getDouble("PlaceLng"));
        UserLatLng = new LatLng(bundle.getDouble("lat"),bundle.getDouble("lng"));


        toolbar = (Toolbar) findViewById(R.id.toobar_withoutmenu);
        toolbar.setTitle(PlaceName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        UserLat = UserLatLng.latitude;
        UserLng = UserLatLng.longitude;
        PlaceLat = PlaceLatLng.latitude;
        PlaceLng = PlaceLatLng.longitude;

        PlaceDetailsParser();
        LoadMap();

        placeName = (TextView) findViewById(R.id.placeName);
        placeAddress = (TextView) findViewById(R.id.placeAddress);
        placePhone = (TextView) findViewById(R.id.placePhone);
        placeWebsite = (TextView) findViewById(R.id.placeWebsite);
        placeDistance = (TextView) findViewById(R.id.placeDistance);

        Button directionbtn = (Button) findViewById(R.id.getDirection);
        directionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri =String.format(Locale.ENGLISH,"http://maps.google.com/maps?saddr="+UserLat+","+UserLng+"&daddr=" + PlaceLat+","+PlaceLng+"");

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

    }

    public void PlaceDetailsParser()
    {
        dialog.show();
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+PlaceId+"&key=AIzaSyCLltShL1vvXLQDOjTOuQlkhzwuPQEMuqA";

        RequestQueue requestQueue = Volley.newRequestQueue(PlaceDetails.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       final JsonDataModel jsonDataModel = JsonDataParser.parsePlaceDetailsData(response);

                        placeName.setText(PlaceName);
                        placeAddress.setText(PlaceAddress);

                        if(jsonDataModel.getPlaceWebsite().equals("N/A"))
                        placeWebsite.setText("Website : "+"Website is not available");
                        else placeWebsite.setText(Html.fromHtml(
                                    "Website : " + "<a href="+jsonDataModel.getPlaceWebsite()+">"+jsonDataModel.getPlaceWebsite()+"</a>" ));
                        placeWebsite.setMovementMethod(LinkMovementMethod.getInstance());

                        if(jsonDataModel.getPlacePhone().equals("N/A"))
                        placePhone.setText("Phone : "+"Phone number is not available");
                        else placePhone.setText("Phone : "+jsonDataModel.getPlacePhone());

                        if(StaticService.distanceBetween(UserLatLng, PlaceLatLng) >= 1000)
                            placeDistance.setText("Distance : "+String.format("%.2f", StaticService.distanceBetween(UserLatLng, PlaceLatLng) * 0.001)+" Km");
                        else
                            placeDistance.setText("Distance : "+String.format("%.0f", StaticService.distanceBetween(UserLatLng, PlaceLatLng))+" meters");

                        dialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Details Parse Error",error.getMessage());
                        dialog.hide();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void LoadMap()
    {


        try
        {
            if(googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);


            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng (UserLat,UserLng))
                    .title("My Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            // fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.terrain)))); //

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(PlaceLat, PlaceLng))
                    .title(PlaceName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(PlaceLat, PlaceLng)).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
