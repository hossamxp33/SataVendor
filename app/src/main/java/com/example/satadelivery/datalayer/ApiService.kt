package com.example.satadelivery.datalayer

import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.Driver
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.DateModel

import com.example.satadelivery.models.current_orders.OrdersItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface APIServices {

    ////////////// Authentication
    @POST("Driverlogin")
    suspend fun login(@Body loginModel: User?): Response<AuthModel>

    //delivers/GetDliveryOrders


    @GET("delivers/GetDliveryCurentOrders")
    suspend fun getCurrentOrders(): ArrayList<OrdersItem>

    //Delivery Orders By Date
    @POST("delivers/GetDliveryOrdersByDate")
    suspend fun getDeliveryOrdersByDate(@Body dateModel: DateModel?): ArrayList<OrdersItem>

// order_status_id
    // قبول 3
    // تسليم 4
    // رفض 5
    @FormUrlEncoded
    @POST("orders/edit/{orderId}")
    suspend fun changeOrderStatus(@Path("orderId") orderId: Int, @Field("order_status_id") status: Int): OrdersItem


    @Multipart
    @Headers("Content-Type: application/json")
    @POST("delivers/edit/{id}")
    suspend fun editDeliveryData(
        @Part img: MultipartBody.Part,
        @Part( "name")name: String,
        @Part( "phone")phone: String,
        @Path("id") id: Int,
        ) : Driver
}


