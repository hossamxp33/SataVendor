package com.example.satadelivery.repository

import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.DateModel
import com.example.satadelivery.models.current_orders.OrdersItem

import retrofit2.Response


/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/


interface DataSource {

    suspend fun getLoginResponse(loginModel: User): Response<AuthModel>

    suspend fun getCurrentOrders(): ArrayList<OrdersItem>

    suspend fun getDeliveryOrdersByDate(dateModel: DateModel?): ArrayList<OrdersItem>
    suspend fun changeOrderStatus(Id:Int,statusId:Int) : OrdersItem

}