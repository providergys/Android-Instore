package com.teaera.teaeracafe.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaeracafe.net.Model.CategoryInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 10/09/2017.
 */

public class CategoryPrefs {

    private static final String PREFIX = "categories";
    private static final String CATEGORIES = "categories";

    private static CategoryPrefs instance;

    private static SharedPreferences preferences;

    public static CategoryPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryPrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setCategories(Context context, ArrayList<CategoryInfo> categories) {
        Gson gson = new Gson();
        String jsonCategoriesString = gson.toJson(categories);

        CategoryPrefs.getInstance(context).getPreferences().edit().putString(CATEGORIES, jsonCategoriesString).apply();
    }

    public static ArrayList<CategoryInfo> getCategories(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CategoryInfo>>() {}.getType();
        final String jsonString = CategoryPrefs.getInstance(context).getPreferences().getString(CATEGORIES, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearCategories(Context context) {
        CategoryPrefs.getInstance(context).getPreferences().edit().clear().commit();
    }

    public static boolean isCategoriesExists(Context context) {
        return CategoryPrefs.getInstance(context).getPreferences().contains(CATEGORIES);
    }

}
