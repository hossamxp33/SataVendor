package com.example.satadelivery.models.daily_order

data class BillingAddress(
    var address: String,
    var apartment_number: String,
    var area_id: String,
    var created_at: String,
    var first_name: String,
    var floor_number: String,
    var id: Int,
    var last_name: String,
    var latitude: Double,
    var longitude: Double,
    var notes: String,
    var phone: String,
    var updated_at: String,
    var user_id: Int
)