package com.codesroots.satavendor.repository

import com.codesroots.satavendor.di.IoDispatcher
import com.codesroots.satavendor.models.auth.Driver
import com.codesroots.satavendor.models.current_orders.DateModel
import com.codesroots.satavendor.models.current_orders.OrderStatus
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.satafood.core.entities.token.Token
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

        fun getOrders(id :Int?): Flow<Result<ArrayList<OrdersItem>>> =
        flow {
            emit(Datasources.getCurrentOrders(id))
        }
            .map { Result.success(it) }
            .retry(retries = 4) { t -> (t is IOException).also { if (it) {
                delay(1000 )
            }}}
            .catch {
                    throwable ->  emit(Result.failure(throwable)) }
            .flowOn(ioDispatcher)


        fun getDeliveris(dateModel: DeliveryItem?): Flow<Result<ArrayList<DeliveryItem>>> =
        flow {
            emit(Datasources.getDeliveris(dateModel!!))
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

    suspend  fun changeOrderStatus(order_id:Int,data: OrderStatus): Flow<Result<OrderStatus>> =
        flow {
            emit(Datasources.changeOrderStatus(order_id,data))
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


    suspend  fun updateUserToken(userId: Int, token: Token): Flow<Result<Int>> =
        flow {
            emit(Datasources.updateUserToken(userId, token))
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