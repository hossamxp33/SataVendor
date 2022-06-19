package com.sata.satadelivery.presentation.history_order_fragment.mvi


import com.sata.satadelivery.helper.UserError
import com.sata.satadelivery.models.current_orders.OrdersItem


data class MainViewState(
    var data: ArrayList<OrdersItem>?=null,
    var filterData : ArrayList<OrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    val progress:  Boolean? = null,
    var error: UserError?=null
)