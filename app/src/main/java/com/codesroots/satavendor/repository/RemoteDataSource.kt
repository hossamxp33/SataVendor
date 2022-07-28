package com.codesroots.satavendor.repository


import androidx.test.orchestrator.junit.BundleJUnitUtils
import com.codesroots.satavendor.datalayer.APIServices
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
import javax.inject.Inject

/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/

class RemoteDataSource @Inject constructor(private val ApiService: APIServices)
    : DataSource {


    override suspend fun getLoginResponse(loginModel: User): Response<AuthModel> {
        return ApiService.login(loginModel)
    }


    override suspend fun registerToken(loginModel: AuthModel): Response<AuthModel> {
        return ApiService.registerToken(loginModel)
    }

    override suspend fun sendNotificationToDevice(loginModel: AuthModel): Response<AuthModel> {
        return ApiService.sendNotificationToDevice(loginModel)
    }



    override suspend fun getDeliveris(delivery: DeliveryItem):ArrayList<DeliveryItem>{
        return ApiService.getDeliveris(delivery)
    }


    override suspend fun getCurrentOrders(id:Int?): ArrayList<OrdersItem> =
       runCatching { ApiService.getCurrentOrders(id!!) }
           .getOrElse { throw it }


    override suspend fun getDeliveryOrdersByDate(dateModel: DateModel?): ArrayList<OrdersItem> =
       runCatching { ApiService.getDeliveryOrdersByDate(dateModel) }
           .getOrElse { throw it }

    override suspend fun changeOrderStatus(order_id:Int,data: OrderStatus): OrderStatus {
        return ApiService.changeOrderStatus(order_id,data)
    }

    override suspend fun updateUserToken(userId: Int, token: Token): Int {
        return ApiService.updateUserToken(userId, token)
    }



    //changeDeliveryStatus
    override suspend fun editDeliveryData(file: MultipartBody.Part?,name : String?,phone:String? ,id: Int?): Driver {
        return ApiService.editDeliveryData(file!!,name!!,phone!!,id!!)
    }

    override suspend fun getBranchData(branchId: Int?): Response<Delivery>  {
        return ApiService.getBranchData(branchId!!)
    }


    //deliversOrdersCanceled
    override suspend fun deliversOrdersCanceled(data: OrdersItem): OrdersItem  {
        return ApiService.deliversOrdersCanceled(data)
    }
}