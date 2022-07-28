package com.codesroots.satavendor.models.auth

data class AuthModel(
    var token: String?=null,
    var user_id: Int?=null,
    val title: String? = null,
    val message: String? = null,
    var user: User?=null,

)