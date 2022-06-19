package com.sata.satadelivery.presentation.current_order_fragment.mvi


import com.sata.satadelivery.helper.UserError
import com.sata.satadelivery.models.current_orders.OrdersItem


data class MainViewState(
    var data: ArrayList<OrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    var progress:  Boolean? = null,
    var error: UserError?=null,
    var cliendLatitude :Double ? = null,
    var cliendLongitude :Double ? = null


)