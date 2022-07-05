package com.sata.satadelivery.models.auth

data class Driver(
    var branch_id: Int?=null,
    var created: String?=null,
    var id: Int?=null,
    var modified: String?=null,
    var name: String?=null,
    var photo: String?=null,
    var mobile: String?=null,
    var room_id: String?=null,
    var salary: Int?=null,
    var delivery_information :Int?=null,
    var branches: Branches,
    var is_online: Int,
    var user_id: Int
)