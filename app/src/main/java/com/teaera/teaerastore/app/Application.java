package com.teaera.teaerastore.app;

import com.teaera.teaerastore.net.ServerAPI;
import com.teaera.teaerastore.utils.FontsOverride;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 24/08/2017.
 */

public class Application extends android.app.Application {

    private static Retrofit retrofit;
    private static ServerAPI serverApi;

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
    }

    public static ServerAPI getServerApi() {
        return serverApi;
    }

}
