package com.example.satadelivery.models.branch_orders

data class Branches(
    var address: String,
    var created: String,
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var modified: String,
    var name: String,
    var name_en: String,
    var phone: String,
    var phone_two: String,
    var restaurant: Restaurant,
    var restaurantsId: Int,
    var status: String,
    var user_id: Int
)