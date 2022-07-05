package com.sata.satadelivery.presentation.current_order_fragment.mvi


sealed class MainIntent(open val viewState: MainViewState? = null,open val lat : Double?= null) {

    data class Initialize(override val viewState: MainViewState) : MainIntent()

    data class getLatLong(override val viewState: MainViewState) : MainIntent()

    data class ErrorDisplayed(override val viewState: MainViewState) : MainIntent()

}
