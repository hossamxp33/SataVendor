package com.example.satadelivery.repository


import com.example.satadelivery.datalayer.APIServices
import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.CurrentOrdersItem
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem

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

    override suspend fun getDeliveryOrders(): ArrayList<DeliveryOrdersItem> =
        runCatching { ApiService.getDeliveryOrders() }
            .getOrElse { throw it }

    override suspend fun getCurrentOrders(): ArrayList<CurrentOrdersItem> =
       runCatching { ApiService.getCurrentOrders() }
           .getOrElse { throw it }


}