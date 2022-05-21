package com.example.satadelivery.models.branch_orders

data class Weekorder(
    var CashOrders: Int,
    var Ordertotals: Int,
    var billing_address_id: Int,
    var branch_id: Int,
    var branches: Branches,
    var commission: Int,
    var coupon_id: Int,
    var created: String,
    var delivery_id: Int,
    var discount: Int,
    var id: Int,
    var modified: String,
    var month: String,
    var notes: String,
    var offer_id: Int,
    var order_status: OrderStatus,
    var order_status_id: Int,
    var ordercount: Int,
    var paymenttype_id: Int,
    var taxes: Int,
    var total: Int,
    var user_id: Int,
    var visaOrders: Int
)