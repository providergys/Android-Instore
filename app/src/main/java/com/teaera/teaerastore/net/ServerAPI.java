package com.teaera.teaerastore.net;

import com.teaera.teaerastore.net.Request.SignInRequest;
import com.teaera.teaerastore.net.Response.BaseResponse;
import com.teaera.teaerastore.net.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by admin on 24/08/2017.
 */

public interface ServerAPI {

    String BASE_URL = "http://34.228.81.219/API/";
    //String DEVICE = "android";

    @POST("Index.php?Service=Login")
    Call<UserResponse> signIn(
            @Body SignInRequest request
    );

}

