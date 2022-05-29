package com.example.satadelivery.presentation.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.satadelivery.helper.PreferenceHelper
import com.example.satadelivery.models.auth.AuthModel
import com.example.satadelivery.models.auth.User
import com.example.satadelivery.repository.RemoteDataSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val Datasources: RemoteDataSource,
) : ViewModel() {

    @Inject
    lateinit var Pref: PreferenceHelper
    private var job: Job? = null
    var mCompositeDisposable = CompositeDisposable()
    var processVisibility = MutableLiveData(false)

    var rateJob: Job? = null
    var authLD: MutableLiveData<AuthModel>? = null

    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()


    init {
        authLD = MutableLiveData()
    }



    //authentication
    fun login(loginModel: User?) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = Datasources.getLoginResponse(loginModel!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    authLD?.postValue(response.body())

                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }



    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
        rateJob?.cancel()
    }
}