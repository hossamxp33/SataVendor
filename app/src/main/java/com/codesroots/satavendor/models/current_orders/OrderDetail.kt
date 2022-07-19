package com.codesroots.satavendor.models.current_orders

data class OrderDetail(
    var amount: Int?=null,
    var created: String?=null,
    var id: Int?=null,
    var menu_categories_itemId: Int?=null,
    var menu_categories_items: MenuCategoriesItems?=null,
    var modified: String?=null,
    var notes: String?=null,
    var orderId: Int?=null,
    var total: Double?=null,
)
