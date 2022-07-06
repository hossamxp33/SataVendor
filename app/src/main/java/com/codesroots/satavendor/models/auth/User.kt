package com.codesroots.satavendor.models.auth

data class User(
    var active: Int?=null,
    var address: String?=null,
    var branch_id: Int?=null,
    var branches: Branches?=null,
    var code: String?=null,
    var created: String?=null,
    var department: Any?=null,
    var department_id: Int?=null,
    var department_positions: Any?=null,
    var department_positions_id: Int?=null,
    var device_token: String?=null,
    var email: String?=null,
    var email_verified: Int?=null,
    var facebook_id: String?=null,
    var first_name: String?=null,
    var group: Group?=null,
    var id: Int?=null,
    var last_name: String?=null,
    var mobile: String?=null,
    var modified: String?=null,
    var name: String?=null,
    var password: String?=null,
    var photo: String?=null,
    var position: String?=null,
    var restaurant_id: Int?=null,
    var restaurants: Any?=null,
    var room_id: String?=null,
    var user_group_id: Int?=null,
    var username: String?=null,
)