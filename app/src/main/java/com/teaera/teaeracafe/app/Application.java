package com.teaera.teaeracafe.app;

import com.teaera.teaeracafe.net.ServerAPI;
import com.teaera.teaeracafe.utils.FontsOverride;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 24/07/2017.
 */

public class Application extends android.app.Application {

    private static Retrofit retrofit;
    private static ServerAPI serverApi;
    private static boolean redeemed = false;
    private static String server_profile_image_prefix = "http://34.228.81.219/images/profile/";
    private static String server_image_prefix = "http://34.228.81.219/images/img/";

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Montserrat-Light.otf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Regular.otf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Montserrat-Bold.otf");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serverApi = retrofit.create(ServerAPI.class);

        redeemed = false;
    }

    public static ServerAPI getServerApi() {
        return serverApi;
    }

    public static boolean getIsRedeemed() {
        return redeemed;
    }

    public static void setIsRedeemed(boolean redeemed) {
        redeemed = redeemed;
    }

    public static String getServerProfileImagePrefix() {
        return server_profile_image_prefix;
    }

    public static String getServerImagePrefix() {
        return server_image_prefix;
    }
}
