package com.codesroots.satavendor.presentation.profile_fragment.viewmodel


import androidx.lifecycle.MutableLiveData
import com.codesroots.satavendor.helper.BaseViewModel
import com.codesroots.satavendor.models.auth.Driver
import com.codesroots.satavendor.models.delivery.Delivery
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.MainViewState
import com.codesroots.satavendor.repository.DataRepo
import com.codesroots.satavendor.repository.RemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileViewmodel @Inject constructor(private val DateRepoCompnay: DataRepo, private val Datasources: RemoteDataSource) :
    BaseViewModel<MainViewState>() {


    protected val uiState : MutableStateFlow<MainViewState?> = MutableStateFlow(MainViewState())

    val state: MutableStateFlow<MainViewState?> get() = uiState

    private var job: Job? = null

    protected val changeDriveruiState : MutableStateFlow<Driver>? = null

    val DriverState: MutableStateFlow<Driver>? get() = changeDriveruiState
    var deliveryItemLD: MutableLiveData<Delivery>? = null

    init {
        deliveryItemLD = MutableLiveData()
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
    fun getDeliversStatus(Id:Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = Datasources.getDeliversStatus(Id)
            withContext(Dispatchers.Main) {
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
