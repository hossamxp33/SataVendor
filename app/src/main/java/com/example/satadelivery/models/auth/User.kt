package com.example.satadelivery.models.auth

import com.example.satadelivery.models.auth.Driver

data class User(
    var active: Int,
    var address: String,
    var branch_id: Int,
    var created: String,
    var department_id: Int,
    var department_positions_id: Int,
    var device_token: String,
    var driver: Driver,
    var email: String,
    var email_verified: Int,
    var facebook_id: String,
    var id: Int,
    var mobile: String,
    var modified: Any,
    var name: String,
    var password: String,
    var position: String,
    var restaurant_id: Int,
    var user_group_id: Int,
    var username: String
)