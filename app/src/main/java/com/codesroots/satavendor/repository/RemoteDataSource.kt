package com.codesroots.satavendor.repository


import com.codesroots.satavendor.datalayer.APIServices
import com.codesroots.satavendor.models.auth.AuthModel
import com.codesroots.satavendor.models.auth.Driver
import com.codesroots.satavendor.models.auth.User
import com.codesroots.satavendor.models.current_orders.DateModel
import com.codesroots.satavendor.models.current_orders.OrderStatus
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.Delivery
import okhttp3.MultipartBody

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

    override suspend fun changeOrderStatus(order_id:Int,data: OrderStatus): OrderStatus {
        return ApiService.changeOrderStatus(order_id,data)
    }

    override suspend fun changeDeliveryStatus(id:Int?,statusId:Int): OrdersItem {
        return ApiService.changeDeliveryStatus(id!!,statusId)
    }


    //changeDeliveryStatus
    override suspend fun editDeliveryData(file: MultipartBody.Part?,name : String?,phone:String? ,id: Int?): Driver {
        return ApiService.editDeliveryData(file!!,name!!,phone!!,id!!)
    }

    override suspend fun getDeliversStatus(id: Int?): Response<Delivery>  {
        return ApiService.getDeliversStatus(id!!)
    }


    //deliversOrdersCanceled
    override suspend fun deliversOrdersCanceled(data: OrdersItem): OrdersItem  {
        return ApiService.deliversOrdersCanceled(data)
    }
}