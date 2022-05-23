package com.example.satadelivery.presentation.daily_order_fragment.mvi

import com.example.satadelivery.helper.UserError
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem


data class MainViewState(
    var data: ArrayList<DeliveryOrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    val progress:  Boolean? = null,
    var error: UserError?=null

)