package com.teaera.teaeracafe.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaeracafe.net.Model.PromotedMenuInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 27/09/2017.
 */

public class PromotedMenuPrefs {

    private static final String PREFIX = "promoted";
    private static final String PROMOTED = "promoted";

    private static PromotedMenuPrefs instance;

    private static SharedPreferences preferences;

    public static PromotedMenuPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new PromotedMenuPrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setPromotedMenu(Context context, ArrayList<PromotedMenuInfo> promoted) {
        Gson gson = new Gson();
        String jsonPromotedString = gson.toJson(promoted);

        PromotedMenuPrefs.getInstance(context).getPreferences().edit().putString(PROMOTED, jsonPromotedString).apply();
    }

    public static ArrayList<PromotedMenuInfo> getPromotedMenu(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PromotedMenuInfo>>() {}.getType();
        final String jsonString = PromotedMenuPrefs.getInstance(context).getPreferences().getString(PROMOTED, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearPromotedMenu(Context context) {
        PromotedMenuPrefs.getInstance(context).getPreferences().edit().clear().commit();
    }

    public static boolean isPromotedMenuExists(Context context) {
        return PromotedMenuPrefs.getInstance(context).getPreferences().contains(PROMOTED);
    }

}
