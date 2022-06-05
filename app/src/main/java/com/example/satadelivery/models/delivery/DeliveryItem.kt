package com.example.satadelivery.models.delivery

data class DeliveryItem(
    var branch_id: Int,
    var branches: Branches,
    var created: String,
    var id: Int,
    var is_online: Int,
    var mobile: String,
    var modified: String,
    var name: String,
    var photo: String,
    var room_id: String,
    var salary: Int,
    var user_id: Int
)