package com.codesroots.satavendor.models

data class OrderDetail(
    var amount: Int,
    var created: String,
    var id: Int,
    var menu_categories_itemId: Int,
    var menu_categories_items: MenuCategoriesItems,
    var modified: String,
    var notes: String,
    var orderId: Int,
    var order_details_options: List<OrderDetailsOption>,
    var total: Int
)