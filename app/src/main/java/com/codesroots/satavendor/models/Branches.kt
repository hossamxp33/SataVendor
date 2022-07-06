package com.codesroots.satavendor.models

data class Branches(
    var activated: Int,
    var address: String,
    var address_en: String,
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
    var status_id: Int,
    var user_id: Int
)