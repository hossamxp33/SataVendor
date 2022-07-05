package com.sata.satadelivery.models.delivery

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
    var restaurantsId: Int,
    var status: String,
    var user_id: Int
)