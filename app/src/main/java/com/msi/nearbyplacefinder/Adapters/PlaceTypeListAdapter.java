package com.msi.nearbyplacefinder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msi.nearbyplacefinder.ApplicationHelper.StaticService;
import com.msi.nearbyplacefinder.R;

/**
 * Created by MSi on 10/28/2015.
 */
public class PlaceTypeListAdapter extends BaseAdapter {

    private Context context;
    private String []PlaceType;
    private LayoutInflater layoutInflater;
    private View placeListView;



    public PlaceTypeListAdapter(Context context, String []PlaceType) {
        this.context = context;
        this.PlaceType = PlaceType;
    }

    @Override
    public int getCount() {
        return PlaceType.length;
    }

    @Override
    public Object getItem(int position) {
        return PlaceType[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        placeListView = layoutInflater.inflate(R.layout.placetypelist, null, false);

        TextView placeView = (TextView) placeListView.findViewById(R.id.placeType);
        ImageView imageView = (ImageView) placeListView.findViewById(R.id.placeImage);

        placeView.setText(PlaceType[position]);
        imageView.setImageResource(StaticService.placeImage(position));

        return placeListView;
    }
}
