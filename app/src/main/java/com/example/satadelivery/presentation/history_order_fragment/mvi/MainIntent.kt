package com.example.satadelivery.presentation.history_order_fragment.mvi

import com.example.satadelivery.models.current_orders.DateModel


sealed class MainIntent(open val viewState: MainViewState? = null,open val dateModel: DateModel?=null) {

    data class Initialize(override val viewState: MainViewState,override val dateModel: DateModel?) : MainIntent()

    data class ErrorDisplayed(override val viewState: MainViewState) : MainIntent()

}
