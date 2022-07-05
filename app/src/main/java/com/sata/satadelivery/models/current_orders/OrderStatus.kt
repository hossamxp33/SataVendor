package com.sata.satadelivery.models.current_orders

data class OrderStatus(
    var delivery_id: Int?=null,
    var order_status_id: Int?=null,
    var delivery_comment :String?=null,
)