package com.codesroots.satavendor.models

data class Offers(
    var approval: Int,
    var branch_id: Int,
    var created: Any,
    var department_id: Int,
    var department_message: String,
    var description: String,
    var description_en: String,
    var end_date: String,
    var id: Int,
    var img: String,
    var menu_categories_items_id: Int,
    var modified: String,
    var percentage: Int,
    var publish: Int,
    var restaurant_id: Int,
    var start_date: String
)