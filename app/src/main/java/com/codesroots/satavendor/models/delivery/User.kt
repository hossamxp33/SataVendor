package com.codesroots.satavendor.models.delivery

data class User(
    var active: Int,
    var address: String,
    var branch_id: Int,
    var code: String,
    var created: String,
    var department_id: Int,
    var department_positions_id: Int,
    var device_token: String,
    var email: String,
    var email_verified: Int,
    var facebook_id: String,
    var first_name: String,
    var id: Int,
    var last_name: String,
    var mobile: String,
    var modified: String,
    var name: String,
    var password: String,
    var photo: String,
    var position: String,
    var restaurant_id: Int,
    var room_id: String,
    var user_group_id: Int,
    var username: String
)