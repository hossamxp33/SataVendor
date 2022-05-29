package com.example.satadelivery.repository

import com.example.satadelivery.di.IoDispatcher
import com.example.satadelivery.models.current_orders.DateModel
import com.example.satadelivery.models.current_orders.OrdersItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class DataRepo @Inject constructor(
    private val Datasources: DataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
)  {

        val getOrders: Flow<Result<ArrayList<OrdersItem>>> =
        flow {
            emit(Datasources.getCurrentOrders())
        }
            .map { Result.success(it) }
            .retry(retries = 4) { t -> (t is IOException).also { if (it) {
                delay(1000 )
            }}}
            .catch {
                    throwable ->  emit(Result.failure(throwable)) }
            .flowOn(ioDispatcher)


        fun getDeliveryOrdersByDate(dateModel: DateModel?): Flow<Result<ArrayList<OrdersItem>>> =
        flow {
            emit(Datasources.getDeliveryOrdersByDate(dateModel))
        }
            .map { Result.success(it) }
            .retry(retries = 4) { t -> (t is IOException).also { if (it) {
                delay(1000 )
            }}}
            .catch {
                    throwable ->  emit(Result.failure(throwable)) }
            .flowOn(ioDispatcher)




    //changeOrderStatus

    suspend  fun changeOrderStatus(Id:Int,statusId:Int): Flow<Result<OrdersItem>> =
        flow {
            emit(Datasources.changeOrderStatus(Id,statusId))
        }
            .map {
                Result.success(it)
            }
            .retry(retries = 4) { t -> (t is IOException).also { if (it) {
                delay(1000)
            }}}
            .catch {

                    throwable ->  emit(Result.failure(throwable)) }
            .flowOn(ioDispatcher)

}