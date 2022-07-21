package com.codesroots.satavendor.presentation.current_order_fragment.mvi


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codesroots.satavendor.helper.BaseViewModel
import com.codesroots.satavendor.models.current_orders.OrderStatus
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.Delivery
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.repository.DataRepo
import com.codesroots.satavendor.repository.RemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class CurrentOrderViewModel @Inject constructor(private val DateRepoCompnay: DataRepo, private val Datasources: RemoteDataSource,) :
    BaseViewModel<MainViewState>() {

    val intents : Channel<MainIntent> = Channel<MainIntent>(Channel.UNLIMITED)

    protected val uiState : MutableStateFlow<MainViewState?> = MutableStateFlow(MainViewState())

    val state: MutableStateFlow<MainViewState?> get() = uiState

    private var job: Job? = null

    var  mclientLatitude = MutableLiveData<Double>()
    var  mclientLongitude = MutableLiveData<Double>()

    protected val ChangeStatusuiState : MutableStateFlow<OrdersItem>? = null

    val OrderState: MutableStateFlow<OrdersItem>? get() = ChangeStatusuiState

    var deliveryItemLD: MutableLiveData<Delivery>? = null
    var deliveriesDataLD = MutableLiveData< ArrayList<DeliveryItem>>()

    var OrderStateLD: MutableLiveData<OrderStatus>? = null

    protected val getStatusState : MutableStateFlow<DeliveryItem>? = null

    val deliveryState: MutableStateFlow<DeliveryItem>? get() = getStatusState

    protected val getDeliveriesData : MutableStateFlow<ArrayList<DeliveryItem>>? = null

    val deliveriesState: MutableStateFlow<ArrayList<DeliveryItem>>? get() = getDeliveriesData

    init {
         getIntent()
        mclientLatitude = MutableLiveData()
        mclientLatitude = MutableLiveData()
        deliveryItemLD = MutableLiveData()
        OrderStateLD = MutableLiveData()
        deliveriesDataLD = MutableLiveData()
    }
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    fun getIntent() {

        job = viewModelScope.launch() {
            uiState.value = MainViewState().copy(progress = true)
            uiState.value = MainViewState().copy(noOrderYet = true)

            intents.receiveAsFlow().collect {
                uiState.value = (mapIntentToViewState(it, DateRepoCompnay))
            }
        }
    }

    fun getLatLong(latitude: Double?,longitude:Double?) {
         mclientLatitude.postValue(latitude!!)
        mclientLongitude.postValue(longitude!!)

    }


    fun changeOrderStatus(Id:Int,data: OrderStatus) {
        job = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
            val response = DateRepoCompnay.changeOrderStatus(Id,data)
            withContext(Dispatchers.Main+coroutineExceptionHandler) {
                (response.collect {
                    runCatching {
                        OrderStateLD?.value = it.getOrNull()!!

                    }.getOrElse {
                        onError("Error : ${it.message} ")

                    }
                })
            }
        }

    }

    fun deliversOrdersCanceled(data:OrdersItem) {
        job = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
            val response = DateRepoCompnay.deliversOrdersCanceled(data)
            withContext(Dispatchers.Main+coroutineExceptionHandler) {
                (response.collect {
                    runCatching {
                        OrderState?.value = it.getOrNull()!!

                    }.getOrElse {
                        onError("Error : ${it.message} ")

                    }
                })
            }
        }

    }
    fun getDeliveris(data:DeliveryItem) {
        job = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
            val response = DateRepoCompnay.getDeliveris(data)
            withContext(Dispatchers.Main+coroutineExceptionHandler) {
                (response.collect {
                    runCatching {
                        deliveriesDataLD.value = it.getOrNull()!!

                    }.getOrElse {
                        onError("Error : ${it.message} ")

                    }
                })
            }
        }

    }
//changeDeliveryStatus
fun changeDeliversStatus(Id:Int,statusId:Int) {
    job = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
        val response = DateRepoCompnay.changeDeliveryStatus(Id, statusId)
        withContext(Dispatchers.Main) {
            (response.collect {
                runCatching {
                    OrderState?.value = it.getOrNull()!!

                }.getOrElse {
                    onError("Error : ${it.message} ")

                }
            })
        }
    }
}

    fun getBranchData(Id:Int) {
        job = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
            val response = Datasources.getBranchData(Id)
            withContext(Dispatchers.Main
            +coroutineExceptionHandler) {
                if (response.isSuccessful) {
                    deliveryItemLD?.postValue(response.body())

                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }



    private fun onError(message: String) {
//        errorMessage.value = message
//        loading.value = false
    }
    override fun onCleared() {
        super.onCleared()
        job!!.cancel()
    }
}
