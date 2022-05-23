package com.example.satadelivery.presentation.daily_order_fragment.mvi


sealed class MainIntent(open val viewState: MainViewState? = null) {

    data class Initialize(override val viewState: MainViewState) : MainIntent()

    data class ErrorDisplayed(override val viewState: MainViewState) : MainIntent()

}
