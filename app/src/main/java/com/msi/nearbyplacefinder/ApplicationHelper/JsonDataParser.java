package com.msi.nearbyplacefinder.ApplicationHelper;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.msi.nearbyplacefinder.DataModel.JsonDataModel;
import com.msi.nearbyplacefinder.DataModel.UserInfoDataModel;
import com.msi.nearbyplacefinder.PlaceTypeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MSi on 10/28/2015.
 */
public class JsonDataParser
{
    public ArrayList<JsonDataModel> jsonDataModelArrayList = new ArrayList<>();


    public static JsonDataModel parsePlaceDetailsData(JSONObject response)
    {
        JsonDataModel jsonDataModel = new JsonDataModel();
        try
        {
            JSONObject result = response.getJSONObject("result");
            if(!result.isNull("website"))
            {
                jsonDataModel.setPlaceWebsite(result.getString("website"));
            }
            else {
                jsonDataModel.setPlaceWebsite("N/A");
            }

            if(!result.isNull("international_phone_number"))
            {
                jsonDataModel.setPlacePhone(result.getString("international_phone_number"));
            }
            else {
                jsonDataModel.setPlacePhone("N/A");
            }
        }
        catch (Exception e)
        {
            Log.e("Data Parsing Error",e.getMessage());
        }
        return jsonDataModel;
    }



    public ArrayList<JsonDataModel> jsonDataModelArray (JSONObject jsonObject)
    {
        try
        {
            JSONArray result = jsonObject.getJSONArray("results");

            for (int i=0;i<result.length();i++) {
                JSONObject Result = result.getJSONObject(i);
                JSONObject geometry = Result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");

                JsonDataModel dataModel = new JsonDataModel();
                dataModel.setPlaceId(Result.getString("place_id"));
                dataModel.setPlaceName(Result.getString("name"));
                dataModel.setPlaceAddress(Result.getString("vicinity"));
                dataModel.setPlaceLatitude(location.getDouble("lat"));
                dataModel.setPlaceLongitude(location.getDouble("lng"));

                jsonDataModelArrayList.add(dataModel);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Place Detail Read Error",e.getMessage());
        }
        return jsonDataModelArrayList;
    }
}
