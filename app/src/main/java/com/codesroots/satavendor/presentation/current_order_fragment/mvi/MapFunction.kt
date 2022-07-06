package com.codesroots.satavendor.presentation.current_order_fragment.mvi



import com.codesroots.satavendor.helper.UserError
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.repository.DataRepo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


/**
 * this is the model function in MVI, it's responsibility is to convert intents into views states
 */
suspend fun mapIntentToViewState(
    intent: MainIntent,
    Datarepo: DataRepo,
    loadMainData: suspend () -> Flow<Result<ArrayList<OrdersItem>>> = { Datarepo.getOrders(intent.vendorId) },
) = when (intent) {
    is MainIntent.Initialize -> proceedWithInitialize(loadMainData, intent)
    is MainIntent.ErrorDisplayed -> intent.viewState.copy(error = null)
    is MainIntent.getLatLong -> intent.viewState.copy()


}


private suspend fun proceedWithInitialize(
    loadCart: suspend () -> Flow<Result<ArrayList<OrdersItem>>>,
    intent: MainIntent,
): MainViewState {
    val response = loadCart()
    val data = response.first()
    return runCatching {
        intent.viewState!!.copy(data = (data.getOrThrow()),
            noOrderYet = false,
            error = null,
            progress = false)
    }
        .getOrElse {
            intent.viewState!!.copy(error = UserError.NetworkError(it))
        }

}





