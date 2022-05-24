package com.example.satadelivery.repository


import com.example.satadelivery.datalayer.APIServices
import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.DateModel
import com.example.satadelivery.models.current_orders.OrdersItem

import retrofit2.Response
import javax.inject.Inject

/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/

class RemoteDataSource @Inject constructor(private val ApiService: APIServices)
    : DataSource {


    override suspend fun getLoginResponse(loginModel: User): Response<AuthModel> {
        return ApiService.login(loginModel)
    }


    override suspend fun getCurrentOrders(): ArrayList<OrdersItem> =
       runCatching { ApiService.getCurrentOrders() }
           .getOrElse { throw it }

    override suspend fun getDeliveryOrdersByDate(dateModel: DateModel?): ArrayList<OrdersItem> =
       runCatching { ApiService.getDeliveryOrdersByDate(dateModel) }
           .getOrElse { throw it }


}