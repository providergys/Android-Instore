package com.teaera.teaeracafe.net;

import com.teaera.teaeracafe.net.Request.ForgotPasswordRequest;
import com.teaera.teaeracafe.net.Request.GetMenuRequest;
import com.teaera.teaeracafe.net.Request.GetOrderRequest;
import com.teaera.teaeracafe.net.Request.GetPromotedMenuRequest;
import com.teaera.teaeracafe.net.Request.LoginRequest;
import com.teaera.teaeracafe.net.Request.OrderDetailsRequest;
import com.teaera.teaeracafe.net.Request.OrderRequest;
import com.teaera.teaeracafe.net.Request.RefundOrdersRequest;
import com.teaera.teaeracafe.net.Request.RegisterRequest;
import com.teaera.teaeracafe.net.Request.SocialLoginRequest;
import com.teaera.teaeracafe.net.Request.UpdatePasswordRequest;
import com.teaera.teaeracafe.net.Request.UpdateUserInfoRequest;
import com.teaera.teaeracafe.net.Request.UserProfileRequest;
import com.teaera.teaeracafe.net.Response.BaseResponse;
import com.teaera.teaeracafe.net.Response.GetCategoryResponse;
import com.teaera.teaeracafe.net.Response.GetOrderResponse;
import com.teaera.teaeracafe.net.Response.MenuResponse;
import com.teaera.teaeracafe.net.Response.OrderDetailsResponse;
import com.teaera.teaeracafe.net.Response.PromotedMenuResponse;
import com.teaera.teaeracafe.net.Response.RefundOrdersResponse;
import com.teaera.teaeracafe.net.Response.UserProfileResponse;
import com.teaera.teaeracafe.net.Response.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by admin on 01/08/2017.
 */

public interface ServerAPI {

    String BASE_URL = "http://34.228.81.219/api/";
    //String DEVICE = "android";

    @POST("login")
    Call<UserResponse> login(
            @Body LoginRequest request
    );

    @POST("register")
    Call<UserResponse> register(
            @Body RegisterRequest request
    );

    @POST("socialLogin")
    Call<UserResponse> socialLogin(
            @Body SocialLoginRequest request
    );

    @POST("forgotPassword")
    Call<BaseResponse> forgotPassword(
            @Body ForgotPasswordRequest request
    );

    @POST("updateUserInfo")
    Call<UserProfileResponse> updateUserInfo(
            @Body UpdateUserInfoRequest request
    );

    @POST("updateUserPassword")
    Call<BaseResponse> updatePassword(
            @Body UpdatePasswordRequest request
    );

    @Multipart
    @POST("updateProfilePhoto")
    Call<UserProfileResponse> uploadProfilePhoto(
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part image
    );

    @POST("getUserProfile")
    Call<UserProfileResponse> getUserProfile(
            @Body UserProfileRequest request
    );

    @GET("getCategories")
    Call<GetCategoryResponse> getCategories(
    );

    @POST("getMenus")
    Call<MenuResponse> getMenus(
            @Body GetMenuRequest request
    );

    @POST("getMenu")
    Call<PromotedMenuResponse> getMenu(
            @Body GetPromotedMenuRequest request
    );

    @POST("placeOrder")
    Call<BaseResponse> placeOrderToServer(
            @Body OrderRequest request
    );

    @POST("refundOrders")
    Call<RefundOrdersResponse> refundOrders(
            @Body RefundOrdersRequest request
    );

    @POST("getOrders")
    Call<GetOrderResponse> getOrders(
            @Body GetOrderRequest request
    );

    @POST("getOrderDetails")
    Call<OrderDetailsResponse> getOrderDetails(
            @Body OrderDetailsRequest request
    );

//    @POST("Index.php?Service=UpdateAndroidToken")
//    Call<BaseResponse> updateAndroidToken(
//            @Body UpdateAndroidTokenRequest request
//    );
}

