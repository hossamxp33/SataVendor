package com.codesroots.satavendor.models.auth

data class User(
    var active: Int?=null,
    var address: String?=null,
    var branch_id: Int?=null,
    var created: String?=null,
    var department_id: Int?=null,
    var department_positions_id: Int?=null,
    var device_token: String?=null,
    var driver: Driver?=null,
    var email: String?=null,
    var email_verified: Int?=null,
    var facebook_id: String?=null,
    var id: Int?=null,
    var mobile: String?=null,
    var modified: Any?=null,
    var name: String?=null,
    var password: String?=null,
    var position: String?=null,
    var restaurant_id: Int?=null,
    var room_id: String?=null,
    var user_group_id: Int?=null,
    var username: String?=null
)