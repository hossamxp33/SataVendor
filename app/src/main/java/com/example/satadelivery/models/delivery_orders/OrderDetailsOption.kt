package com.example.satadelivery.models.delivery_orders

data class OrderDetailsOption(
    var created: String,
    var id: Int,
    var menu_options: MenuOptions,
    var menu_options_id: Int,
    var modified: String,
    var order_detailsId: Int
)