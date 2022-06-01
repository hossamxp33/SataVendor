package com.example.satadelivery.models.current_orders

data class OrdersItem(
    var billing_address: BillingAddress,
    var billing_address_id: Int,
    var branch_id: Int,
    var commission: Int,
    var coupon_id: Int,
    var created: String,
    var delivery_id: Int,
    var delivery_serivce: Int,
    var discount: Int,
    var id: Int,
    var modified: String,
    var name: String,
    var notes: String,
    var offer_id: Int,
    var order_details: List<OrderDetail>,
    var order_status_id: Int,
    var paymenttype: Paymenttype,
    var paymenttype_id: Int,
    var taxes: Int,
    var total: Int,
    var user_id: Int
)