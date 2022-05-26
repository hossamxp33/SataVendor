package com.example.satadelivery.presentation.current_order_fragment.mvi


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.satadelivery.helper.BaseViewModel
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.repository.DataRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class CurrentOrderViewModel @Inject constructor(private val DateRepoCompnay: DataRepo) :
    BaseViewModel<MainViewState>() {

    val intents : Channel<MainIntent> = Channel<MainIntent>(Channel.UNLIMITED)

    protected val uiState : MutableStateFlow<MainViewState?> = MutableStateFlow(MainViewState())

    val state: MutableStateFlow<MainViewState?> get() = uiState

    private var job: Job? = null

    var  mclientLatitude = MutableLiveData<Double>()
    var  mclientLongitude = MutableLiveData<Double>()

    protected val ChangeStatusuiState : MutableStateFlow<OrdersItem>? = null

    val OrderState: MutableStateFlow<OrdersItem>? get() = ChangeStatusuiState

    init {
         getIntent()
        mclientLatitude = MutableLiveData()
        mclientLatitude = MutableLiveData()
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
    fun changeOrderStatus(Id:Int,statusId:Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = DateRepoCompnay.changeOrderStatus(Id,statusId)
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
    private fun onError(message: String) {
//        errorMessage.value = message
//        loading.value = false
    }
    override fun onCleared() {

        super.onCleared()
        job!!.cancel()

    }
}
