package com.teaera.teaerastore.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaerastore.net.Model.StoreInfo;

import java.lang.reflect.Type;

/**
 * Created by admin on 24/08/2017.
 */

public class StorePrefs {

    private static final String PREFIX = "store";
    private static final String STORE = "store";
    private static final String LOGGED = "logged";

    private static StorePrefs instance;

    private static SharedPreferences preferences;

    public static StorePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new StorePrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static boolean isLoggedIn(Context context) {
        return StorePrefs.getInstance(context).getPreferences().getBoolean(LOGGED, false);
    }

    public static void setLoggedIn(Context context, Boolean isLogged) {
        StorePrefs.getInstance(context).getPreferences().edit().putBoolean(LOGGED, isLogged).commit();
    }

    public static void saveStoreInfo(Context context, StoreInfo storeInfo) {
        Gson gson = new Gson();
        String jsonEventsString = gson.toJson(storeInfo);

        StorePrefs.getInstance(context).getPreferences().edit().putString(STORE, jsonEventsString).apply();
    }

    public static StoreInfo getStoreInfo(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<StoreInfo>() {}.getType();
        final String jsonString = StorePrefs.getInstance(context).getPreferences().getString(STORE, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearStoreInfo(Context context) {
        StorePrefs.getInstance(context).getPreferences().edit().clear().commit();
    }


    public static void logout(Context context) {
        StorePrefs.getInstance(context).getPreferences().edit().remove(STORE).apply();
    }

}
