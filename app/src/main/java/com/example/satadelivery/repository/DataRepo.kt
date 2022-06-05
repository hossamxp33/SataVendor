package com.example.satadelivery.repository

import com.example.satadelivery.di.IoDispatcher
import com.example.satadelivery.models.auth.Driver
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.DateModel
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.models.delivery.DeliveryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
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



    //changeDeliveryStatus
    suspend  fun changeDeliveryStatus(Id:Int,statusId:Int): Flow<Result<OrdersItem>> =
        flow {
            emit(Datasources.changeDeliveryStatus(Id,statusId))
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

//deliversOrdersCanceled
suspend  fun deliversOrdersCanceled(data:OrdersItem): Flow<Result<OrdersItem>> =
    flow {
        emit(Datasources.deliversOrdersCanceled(data))
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
    //editDeliveryData
    suspend  fun editDeliveryData( file: MultipartBody.Part?, name : String?, phone:String? ,id: Int?,): Flow<Result<Driver>> =
        flow {
            emit(Datasources.editDeliveryData(file,name,phone,id!!))
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