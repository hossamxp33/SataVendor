package com.example.satadelivery.presentation.profile_fragment.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.satadelivery.helper.BaseViewModel
import com.example.satadelivery.models.auth.Driver
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainViewState
import com.example.satadelivery.repository.DataRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileViewmodel @Inject constructor(private val DateRepoCompnay: DataRepo) :
    BaseViewModel<MainViewState>() {


    protected val uiState : MutableStateFlow<MainViewState?> = MutableStateFlow(MainViewState())

    val state: MutableStateFlow<MainViewState?> get() = uiState

    private var job: Job? = null

    protected val changeDriveruiState : MutableStateFlow<Driver>? = null

    val DriverState: MutableStateFlow<Driver>? get() = changeDriveruiState

    init {

    }

    fun editDeliveryData(id: Int?, file: MultipartBody.Part?, name : String?, phone:String? ) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = DateRepoCompnay.editDeliveryData(file,name,phone,id,)
            withContext(Dispatchers.Main) {
                (response.collect {
                    runCatching {
                        DriverState?.value = it.getOrNull()!!

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
