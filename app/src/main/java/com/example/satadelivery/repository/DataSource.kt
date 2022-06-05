package com.example.satadelivery.repository

import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.Driver
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.DateModel
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.models.delivery.Delivery
import com.example.satadelivery.models.delivery.DeliveryItem
import okhttp3.MultipartBody

import retrofit2.Response


/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/


interface DataSource {

    suspend fun getLoginResponse(loginModel: User): Response<AuthModel>

    suspend fun getCurrentOrders(): ArrayList<OrdersItem>

    suspend fun getDeliveryOrdersByDate(dateModel: DateModel?): ArrayList<OrdersItem>
    suspend fun changeOrderStatus(Id:Int,statusId:Int) : OrdersItem

    suspend fun editDeliveryData(img: MultipartBody.Part?, name : String?, phone:String?,id :Int?) : Driver

    suspend fun getDeliversStatus(id :Int?) : Response<Delivery>

    suspend fun changeDeliveryStatus(id :Int?,statusId:Int) : OrdersItem
    suspend fun deliversOrdersCanceled(data:OrdersItem) : OrdersItem



}