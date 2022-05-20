package com.example.satadelivery.repository


import com.example.satadelivery.datalayer.APIServices
import com.example.satadelivery.models.AuthModel
import com.example.satadelivery.models.User

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


}