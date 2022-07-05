package com.codesroots.satavendor.presentation.current_order_fragment.mvi


import com.codesroots.satavendor.helper.UserError
import com.codesroots.satavendor.models.current_orders.OrdersItem


data class MainViewState(
    var data: ArrayList<OrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    var progress:  Boolean? = null,
    var error: UserError?=null,
    var cliendLatitude :Double ? = null,
    var cliendLongitude :Double ? = null


)