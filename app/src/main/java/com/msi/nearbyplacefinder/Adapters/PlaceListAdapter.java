package com.msi.nearbyplacefinder.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.msi.nearbyplacefinder.ApplicationHelper.JsonDataParser;
import com.msi.nearbyplacefinder.ApplicationHelper.StaticService;
import com.msi.nearbyplacefinder.DataModel.JsonDataModel;
import com.msi.nearbyplacefinder.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by MSi on 10/28/2015.
 */
public class PlaceListAdapter extends BaseAdapter {

    private Context context;
    private List<JsonDataModel> PlaceData;
    private int PlaceIndex;
    private LatLng UserPosition;

    private LayoutInflater layoutInflater;
    private View placeListView;

    
    public PlaceListAdapter(Context context,List<JsonDataModel> PlaceData,int PlaceIndex,LatLng userposition) {
        this.context = context;
        this.PlaceData = PlaceData;
        this.PlaceIndex = PlaceIndex;
        this.UserPosition = userposition;
    }

    @Override
    public int getCount() {
        return PlaceData.size();
    }

    @Override
    public Object getItem(int position) {
        return PlaceData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = LayoutInflater.from(context);
        placeListView = layoutInflater.inflate(R.layout.placelist, null, false);
        final JsonDataModel jsonDataModel = PlaceData.get(position);

        TextView placeView = (TextView) placeListView.findViewById(R.id.placeType);
        TextView placeAdressView = (TextView) placeListView.findViewById(R.id.placeadress);
        TextView placeDistanceView = (TextView) placeListView.findViewById(R.id.placedistance);
        ImageView imageView = (ImageView) placeListView.findViewById(R.id.placeImage);

        placeView.setText(jsonDataModel.getPlaceName());
        placeAdressView.setText(jsonDataModel.getPlaceAddress());
        imageView.setImageResource(StaticService.placeImage(PlaceIndex));

       // String e = String.format("%.2f",distanceBetween(new LatLng(23.7862273, 90.3755422), new LatLng(jsonDataModel.getPlaceLatitude(), jsonDataModel.getPlaceLongitude())) * 0.001)+" KM";

        if(StaticService.distanceBetween(new LatLng(UserPosition.latitude, UserPosition.longitude), new LatLng(jsonDataModel.getPlaceLatitude(), jsonDataModel.getPlaceLongitude())) >= 1000)
        {
            placeDistanceView.setText("Distance : "+String.format("%.2f", StaticService.distanceBetween(new LatLng(UserPosition.latitude, UserPosition.longitude), new LatLng(jsonDataModel.getPlaceLatitude(), jsonDataModel.getPlaceLongitude())) * 0.001)+" Km");
        }
        else {
            placeDistanceView.setText("Distance : "+String.format("%.0f", StaticService.distanceBetween(new LatLng(UserPosition.latitude, UserPosition.longitude), new LatLng(jsonDataModel.getPlaceLatitude(), jsonDataModel.getPlaceLongitude())))+" meters");
        }
        return placeListView;
    }
}