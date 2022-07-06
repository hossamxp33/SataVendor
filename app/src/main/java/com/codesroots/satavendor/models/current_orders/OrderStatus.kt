package com.codesroots.satavendor.models.current_orders

data class OrderStatus(
    var orderId: Int?=null,
    var order_status_id: Int?=null,
    var delivery_comment :String?=null,
    var time : Int?=null
)