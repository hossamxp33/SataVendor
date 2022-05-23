package com.example.satadelivery.presentation.current_order_fragment.mvi


import com.example.satadelivery.helper.UserError
import com.example.satadelivery.models.current_orders.CurrentOrdersItem
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem


data class MainViewState(
    var data: ArrayList<CurrentOrdersItem>?=null,
    var noOrderYet : Boolean? = false,
    val progress:  Boolean? = null,
    var error: UserError?=null

)