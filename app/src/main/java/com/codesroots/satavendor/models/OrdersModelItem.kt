package com.codesroots.satavendor.models

data class OrdersModelItem(
    var billing_address: BillingAddress,
    var billing_address_id: Int,
    var branch_id: Int,
    var branches: Branches,
    var canceled_reason: String,
    var commission: Double,
    var created: String,
    var delivery_comment: String,
    var delivery_id: Int,
    var delivery_serivce: Int,
    var delivery_time: Int,
    var discount: Int,
    var drivers: Drivers,
    var id: Int,
    var modified: String,
    var notes: String,
    var offer_id: Int,
    var offers: Offers,
    var order_details: List<OrderDetail>,
    var order_status: OrderStatus,
    var order_status_id: Int,
    var paymenttype: Paymenttype,
    var paymenttype_id: Int,
    var takeway: Int,
    var taxes: Int,
    var total: Int,
    var user_id: Int,
    var users: Users,
    var wallets: Any,
    var wallets_id: Int
)