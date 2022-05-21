package com.example.satadelivery.repository

import com.example.satadelivery.di.IoDispatcher
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class DataRepo @Inject constructor(
    private val Datasources: DataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
)  {
    //getUserOrders
    val getDeliveryOrders: Flow<Result<ArrayList<DeliveryOrdersItem>>> =
        flow {
            emit(Datasources.getDeliveryOrders())
        }
            .map { Result.success(it) }
            .retry(retries = 4) { t -> (t is IOException).also { if (it) {


                delay(1000 )

            }}}
            .catch {

                    throwable ->  emit(Result.failure(throwable)) }
            .flowOn(ioDispatcher)
}