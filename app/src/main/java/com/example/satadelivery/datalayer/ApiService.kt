package com.example.satadelivery.datalayer

import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.DateModel

import com.example.satadelivery.models.current_orders.OrdersItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


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



}


