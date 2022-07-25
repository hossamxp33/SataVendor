package com.codesroots.satavendor.repository

import com.codesroots.satavendor.models.auth.AuthModel
import com.codesroots.satavendor.models.auth.Driver
import com.codesroots.satavendor.models.auth.User
import com.codesroots.satavendor.models.current_orders.DateModel
import com.codesroots.satavendor.models.current_orders.OrderStatus
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.Delivery
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.satafood.core.entities.token.Token
import okhttp3.MultipartBody

import retrofit2.Response


/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/


interface DataSource {

    suspend fun getLoginResponse(loginModel: User): Response<AuthModel>
    suspend fun getDeliveris(deliveryItem: DeliveryItem): ArrayList<DeliveryItem>

    suspend fun getCurrentOrders(id :Int?): ArrayList<OrdersItem>

    suspend fun updateUserToken(userId: Int, token: Token): Int

    suspend fun getDeliveryOrdersByDate(dateModel: DateModel?): ArrayList<OrdersItem>
    suspend fun changeOrderStatus(order_id:Int,data: OrderStatus) : OrderStatus

    suspend fun editDeliveryData(img: MultipartBody.Part?, name : String?, phone:String?,id :Int?) : Driver

    suspend fun getBranchData(id :Int?) : Response<Delivery>

    suspend fun deliversOrdersCanceled(data:OrdersItem) : OrdersItem



}