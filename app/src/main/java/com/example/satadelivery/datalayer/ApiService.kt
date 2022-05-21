package com.example.satadelivery.datalayer

import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.branch_orders.Weekorder
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem
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

    @GET("delivers/GetDliveryOrders")
    suspend fun getDeliveryOrders(): ArrayList<DeliveryOrdersItem>

    //Delivery Orders By Date
    @POST("delivers/GetDliveryOrdersByDate")
    suspend fun getDeliveryOrdersByDate(@Body loginModel: User?): Response<AuthModel>

    //delivers/indexForBranch
    @GET("delivers/indexForBranch")
    suspend fun getIndexForBranch(): AuthModel

    //edit/3
    @POST("orders/edit/3")
    @Headers("Content-Type: Application/json")
    suspend fun assignToDelivery(@Body loginModel: User?): Response<AuthModel>

    //Restaurant/edit/5
    @POST("Restaurant/edit/5")
    suspend fun topTenVendor(@Body loginModel: User?): Response<AuthModel>

    //orders/getorderReportForBranch/5
    @GET("orders/getorderReportForBranch/5")
    suspend fun getOrderReportForBranch(): Response<List<Weekorder>>


    //VerificationQuestions/index
    @GET("VerificationQuestions/index")
    suspend fun getVerificationQuestions(): Response<AuthModel>

    //DepartmentPositions/getPositionsByDepartment
    @POST("DepartmentPositions/getPositionsByDepartment")
    suspend fun getPositionsByDepartment(@Body loginModel: User?): Response<AuthModel>

}


