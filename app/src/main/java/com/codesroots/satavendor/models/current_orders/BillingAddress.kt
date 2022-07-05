package com.codesroots.satavendor.models.current_orders

data class BillingAddress(
    var address: String?=null,
    var apartment_number: String?=null,
    var area_id: String?=null,
    var created_at: String?=null,
    var first_name: String?=null,
    var floor_number: String?=null,
    var id: Int?=null,
    var last_name: String?=null,
    var latitude: Double?=null,
    var longitude: Double?=null,
    var notes: String?=null,
    var phone: String?=null,
    var updated_at: String?=null,
    var user_id: Int?=null
)