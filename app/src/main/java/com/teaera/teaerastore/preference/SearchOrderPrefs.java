package com.teaera.teaerastore.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaerastore.net.Model.OrderInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 24/10/2017.
 */

public class SearchOrderPrefs {

    private static final String PREFIX = "search";
    private static final String SEARCH = "search";
    private static final String SEARCHED = "searched";

    private static SearchOrderPrefs instance;

    private static SharedPreferences preferences;

    public static SearchOrderPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new SearchOrderPrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static boolean isSearched(Context context) {
        return SearchOrderPrefs.getInstance(context).getPreferences().getBoolean(SEARCHED, false);
    }

    public static void setSearched(Context context, Boolean isSearched) {
        SearchOrderPrefs.getInstance(context).getPreferences().edit().putBoolean(SEARCHED, isSearched).commit();
    }

    public static void saveSearchOrders(Context context, ArrayList<OrderInfo> orders) {
        Gson gson = new Gson();
        String jsonEventsString = gson.toJson(orders);

        SearchOrderPrefs.getInstance(context).getPreferences().edit().putString(SEARCH, jsonEventsString).apply();
    }

    public static ArrayList<OrderInfo> getSearchOrders(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<OrderInfo>>() {}.getType();
        final String jsonString = SearchOrderPrefs.getInstance(context).getPreferences().getString(SEARCH, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearSearchOrders(Context context) {
        SearchOrderPrefs.getInstance(context).getPreferences().edit().clear().commit();
    }

}
