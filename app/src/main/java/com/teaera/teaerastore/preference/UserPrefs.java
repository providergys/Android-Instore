package com.teaera.teaerastore.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaerastore.net.Model.UserData;

import java.lang.reflect.Type;

/**
 * Created by admin on 24/08/2017.
 */

public class UserPrefs {

    private static final String PREFIX = "user";
    private static final String USER = "user";
    private static final String LOGGED = "logged";

    private static UserPrefs instance;

    private static SharedPreferences preferences;

    public static UserPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new UserPrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static boolean isLoggedIn(Context context) {
        return UserPrefs.getInstance(context).getPreferences().getBoolean(LOGGED, false);
    }

    public static void setLoggedIn(Context context, Boolean isLogged) {
        UserPrefs.getInstance(context).getPreferences().edit().putBoolean(LOGGED, isLogged).commit();
    }

    public static void saveUserInfo(Context context, UserData.UserInfo userInfo) {
        Gson gson = new Gson();
        String jsonEventsString = gson.toJson(userInfo);

        UserPrefs.getInstance(context).getPreferences().edit().putString(USER, jsonEventsString).apply();
    }

    public static UserData.UserInfo getUserInfo(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<UserData.UserInfo>() {}.getType();
        final String jsonString = UserPrefs.getInstance(context).getPreferences().getString(USER, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearUserInfo(Context context) {
        UserPrefs.getInstance(context).getPreferences().edit().clear().commit();
    }


    public static void logout(Context context) {
        UserPrefs.getInstance(context).getPreferences().edit().remove(USER).apply();
    }

}
