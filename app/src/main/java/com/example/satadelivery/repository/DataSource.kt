package com.example.satadelivery.repository

import com.example.satadelivery.models.AuthModel
import com.example.satadelivery.models.User
import retrofit2.Response


/**
Created by Prokash Sarkar on Tue, January 19, 2021
 **/


interface DataSource {

    suspend fun getLoginResponse(loginModel: User): Response<AuthModel>





}