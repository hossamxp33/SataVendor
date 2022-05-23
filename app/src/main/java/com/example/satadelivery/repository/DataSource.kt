package com.example.satadelivery.repository

import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.CurrentOrdersItem
import com.example.satadelivery.models.daily_order.DailyOrderModelItem
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem
import retrofit2.Response


/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/


interface DataSource {

    suspend fun getLoginResponse(loginModel: User): Response<AuthModel>


    suspend fun getDeliveryOrders(): ArrayList<DeliveryOrdersItem>
    suspend fun getCurrentOrders(): ArrayList<CurrentOrdersItem>

}