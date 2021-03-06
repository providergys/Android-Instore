package com.teaera.teaerastore.net;

import com.teaera.teaerastore.net.Request.AddRewardsRequest;
import com.teaera.teaerastore.net.Request.GetCustomerRequest;
import com.teaera.teaerastore.net.Request.GetOrdersRequest;
import com.teaera.teaerastore.net.Request.GetPrintRequest;
import com.teaera.teaerastore.net.Request.GetStoreRequest;
import com.teaera.teaerastore.net.Request.LoginRequest;
import com.teaera.teaerastore.net.Request.RefundOrderRequest;
import com.teaera.teaerastore.net.Request.SearchOrderRequest;
import com.teaera.teaerastore.net.Request.UpdateOrderRequest;
import com.teaera.teaerastore.net.Request.UpdateStoreRequest;
import com.teaera.teaerastore.net.Response.BaseResponse;
import com.teaera.teaerastore.net.Response.GetOrdersResponse;
import com.teaera.teaerastore.net.Response.GetPrintResponse;
import com.teaera.teaerastore.net.Response.GetStoresResponse;
import com.teaera.teaerastore.net.Response.RefundOrderResponse;
import com.teaera.teaerastore.net.Response.SearchOrdersResponse;
import com.teaera.teaerastore.net.Response.StoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by admin on 24/08/2017.
 */

public interface ServerAPI {

//  String BASE_URL = "http://34.228.81.219/store/api/";
    String BASE_URL = "http://34.230.22.171/store/api/";

    @POST("storeLogin")
    Call<StoreResponse> login(
            @Body LoginRequest request
    );

    @POST("authorizeCode")
    Call<BaseResponse> authorizeCode(
            @Body LoginRequest request
    );

    @POST("getStoreById")
    Call<StoreResponse> getStoreById(
            @Body GetStoreRequest request
    );

    @POST("updateStore")
    Call<StoreResponse> updateStore(
            @Body UpdateStoreRequest request
    );

    @GET("getStores")
    Call<GetStoresResponse> getStores(
    );

    @POST("getNewOrdersByStore")
    Call<GetOrdersResponse> getNewOrders(
            @Body GetOrdersRequest request
    );

    @POST("getCompletedOrdersByStore")
    Call<GetOrdersResponse> getCompletedOrders(
            @Body GetOrdersRequest request
    );

    @POST("refundOrder")
    Call<RefundOrderResponse> refundOrder(@Header("Content-Type") String content_type, @Body RefundOrderRequest request
    );



    @POST("getCustomerInfo")
    Call<GetOrdersResponse> getCustomerInfo(
            @Body GetCustomerRequest request
    );

    @POST("addRewardsToUser")
    Call<BaseResponse> addRewards(
            @Body AddRewardsRequest request
    );

    @POST("searchOrders")
    Call<SearchOrdersResponse> searchOrders(
            @Body SearchOrderRequest request
    );

    @POST("updateOrderStatus")
    Call<BaseResponse> updateOrderStatus(
            @Body UpdateOrderRequest request
    );

    @POST("getPrintData")
    Call<GetPrintResponse> getPrintData(
            @Body GetPrintRequest request
    );

}

