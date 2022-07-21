package com.codesroots.satavendor.datalayer

import com.codesroots.satavendor.models.auth.AuthModel
import com.codesroots.satavendor.models.auth.Driver
import com.codesroots.satavendor.models.auth.User
import com.codesroots.satavendor.models.current_orders.DateModel
import com.codesroots.satavendor.models.current_orders.OrderStatus

import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.Delivery
import com.codesroots.satavendor.models.delivery.DeliveryItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface APIServices {

    ////////////// Authentication
    @POST("login")
    suspend fun login(@Body loginModel: User?): Response<AuthModel>

    ////////////// delivers
    @POST("delivers/indexForBranch")
    suspend fun getDeliveris(@Body delivery: DeliveryItem?): ArrayList<DeliveryItem>

    //delivers/GetDliveryOrders


    @GET("orders/currentorders/{id}/0")
    suspend fun getCurrentOrders(@Path("id") id: Int): ArrayList<OrdersItem>

    @POST("branches/view/{branchId}")
    suspend fun getBranchData(@Path("branchId") id: Int): Response<Delivery>

    //Delivery Orders By Date
    @POST("orders/GetBranchOrdersByDate")
    suspend fun getDeliveryOrdersByDate(@Body dateModel: DateModel?): ArrayList<OrdersItem>

    // order_status_id
    // قبول 3
    // تسليم 4
    // رفض 5

    @POST("orders/edit/{order_id}")
    suspend fun changeOrderStatus(
        @Path("order_id") orderId: Int, @Body  data: OrderStatus): OrderStatus

//    @FormUrlEncoded
//    @POST("orders/edit/{order_id}")
//    suspend fun changeOrderStatus( @Path("order_id") orderId: Int,
    //        @Field("order_status_id") status: Int,
    //        @Field("delivery_id") delivery_id: Int,
    //    ): OrdersItem

    @FormUrlEncoded
    @POST("delivers/edit/{id}")
    suspend fun changeDeliveryStatus(
        @Path("id") id: Int,
        @Field("is_online") status: Int,
    ): OrdersItem

    //deliversOrdersCanceled/add
    @POST("deliversOrdersCanceled/add")
    suspend fun deliversOrdersCanceled(@Body data: OrdersItem): OrdersItem

    @Multipart
    @POST("delivers/edit/{id}")
    suspend fun editDeliveryData(
        @Part img: MultipartBody.Part,
        @Part("name") name: String,
        @Part("mobile") phone: String,
        @Path("id") id: Int,
    ): Driver
}


