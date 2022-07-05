package com.codesroots.satavendor.presentation.history_order_fragment.mvi


import com.codesroots.satavendor.helper.UserError
import com.codesroots.satavendor.models.current_orders.OrdersItem


data class MainViewState(
    var data: ArrayList<OrdersItem>?=null,
    var filterData : ArrayList<OrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    val progress:  Boolean? = null,
    var error: UserError?=null
)