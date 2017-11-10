package com.teaera.teaeracafe.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teaera.teaeracafe.net.Model.CartInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 17/09/2017.
 */

public class CartPrefs {

    private static final String PREFIX = "carts";
    private static final String CARTS = "carts";

    private static CartPrefs instance;

    private static SharedPreferences preferences;

    public static CartPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new CartPrefs();
            preferences = context.getSharedPreferences(PREFIX, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setCarts(Context context, ArrayList<CartInfo> carts) {
        Gson gson = new Gson();
        String jsonCartsString = gson.toJson(carts);

        CartPrefs.getInstance(context).getPreferences().edit().putString(CARTS, jsonCartsString).apply();
    }

    public static ArrayList<CartInfo> getCarts(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartInfo>>() {}.getType();
        final String jsonString = CartPrefs.getInstance(context).getPreferences().getString(CARTS, null);
        if (jsonString != null) {
            return gson.fromJson(jsonString, type);
        } else {
            return null;
        }
    }

    public static void clearCarts(Context context) {
        CartPrefs.getInstance(context).getPreferences().edit().clear().commit();
    }

    public static boolean isCartsExists(Context context) {
        return CartPrefs.getInstance(context).getPreferences().contains(CARTS);
    }


}
