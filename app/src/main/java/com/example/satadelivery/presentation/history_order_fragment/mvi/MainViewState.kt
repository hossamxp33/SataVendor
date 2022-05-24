package com.example.satadelivery.presentation.history_order_fragment.mvi


import com.example.satadelivery.helper.UserError
import com.example.satadelivery.models.current_orders.OrdersItem


data class MainViewState(
    var data: ArrayList<OrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    val progress:  Boolean? = null,
    var error: UserError?=null

)