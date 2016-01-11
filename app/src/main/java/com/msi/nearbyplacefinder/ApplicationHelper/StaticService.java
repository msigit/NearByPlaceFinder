package com.msi.nearbyplacefinder.ApplicationHelper;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.msi.nearbyplacefinder.R;

/**
 * Created by MSi on 10/31/2015.
 */
public class StaticService {

    public static String PlaceType(String PlaceType)
    {
        switch (PlaceType)
        {
            case "Train station": PlaceType = "train_station" ; break;
            case "Subway station": PlaceType = "subway_station" ; break;
            case "Restaurent": PlaceType = "restaurant" ; break;
            case "Post office": PlaceType = "post_office" ; break;
            case "Police station": PlaceType = "police" ; break;
            case "Museum": PlaceType = "museum" ; break;
            case "Mosque": PlaceType = "mosque" ; break;
            case "Hospital": PlaceType = "hospital" ; break;
            case "Grocery or Supermarket": PlaceType = "grocery_or_supermarket" ; break;
            case "Gas station": PlaceType = "gas_station" ; break;
            case "Bus station": PlaceType = "bus_station" ; break;
            case "Bank": PlaceType = "bank" ; break;
            case "Atm booth": PlaceType = "atm" ; break;
            default:break;
        }
        return PlaceType;
    }

    public static int placeImage(int PlaceType)
    {
        switch (PlaceType)
        {
            case 12: PlaceType = R.drawable.train; break;
            case 11: PlaceType = R.drawable.subway; break;
            case 10: PlaceType = R.drawable.restaurent; break;
            case 9: PlaceType = R.drawable.postoffice; break;
            case 8: PlaceType = R.drawable.policestation; break;
            case 7: PlaceType = R.drawable.museum; break;
            case 6: PlaceType = R.drawable.mosque; break;
            case 5: PlaceType = R.drawable.hospital; break;
            case 4: PlaceType = R.drawable.mall; break;
            case 3: PlaceType = R.drawable.gasstation; break;
            case 2: PlaceType = R.drawable.busstation; break;
            case 1: PlaceType = R.drawable.bank; break;
            case 0: PlaceType = R.drawable.atm; break;
            default:break;
        }
        return PlaceType;
    }

    public static Double distanceBetween(LatLng latLng1, LatLng latLng2) {
        if (latLng1 == null || latLng2 == null) {
            return null;
        }
        return SphericalUtil.computeDistanceBetween(latLng1, latLng2);
    }
}
