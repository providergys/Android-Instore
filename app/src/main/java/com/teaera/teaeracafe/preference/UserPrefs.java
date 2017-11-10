package com.teaera.teaeracafe.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaeracafe.net.Model.UserInfo;

import java.lang.reflect.Type;

/**
 * Created by admin on 01/08/2017.
 */

public class UserPrefs {

    private static final String PREFIX = "user";
    private static final String USER = "user";
    private static final String LOGGED = "logged";
    private static final String Tutor = "tutor";

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

    public static boolean getTutor(Context context) {
        return UserPrefs.getInstance(context).getPreferences().getBoolean(Tutor, false);
    }

    public static void setTutor(Context context, Boolean tutor) {
        UserPrefs.getInstance(context).getPreferences().edit().putBoolean(Tutor, tutor).commit();
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        Gson gson = new Gson();
        String jsonEventsString = gson.toJson(userInfo);

        UserPrefs.getInstance(context).getPreferences().edit().putString(USER, jsonEventsString).apply();
    }

    public static UserInfo getUserInfo(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<UserInfo>() {}.getType();
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
