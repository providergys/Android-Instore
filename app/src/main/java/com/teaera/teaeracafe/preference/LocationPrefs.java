package com.teaera.teaeracafe.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaeracafe.net.Model.LocationInfo;
import com.teaera.teaeracafe.net.Model.UserInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 10/09/2017.
 */

public class LocationPrefs {

    private static final String PREFIX = "locations";
    private static final String LOCATIONS = "locations";

    private static LocationPrefs instance;

    private static SharedPreferences preferences;

    public static LocationPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new LocationPrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setLocations(Context context, ArrayList<LocationInfo> locations) {
        Gson gson = new Gson();
        String jsonLocationsString = gson.toJson(locations);

        LocationPrefs.getInstance(context).getPreferences().edit().putString(LOCATIONS, jsonLocationsString).apply();
    }

    public static ArrayList<LocationInfo> getLocations(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<LocationInfo>>() {}.getType();
        final String jsonString = LocationPrefs.getInstance(context).getPreferences().getString(LOCATIONS, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearLocations(Context context) {
        LocationPrefs.getInstance(context).getPreferences().edit().clear().commit();
    }

    public static boolean isLocationsExists(Context context) {
        return LocationPrefs.getInstance(context).getPreferences().contains(LOCATIONS);
    }

}
