package com.sata.satadelivery.presentation.history_order_fragment.mvi


import androidx.lifecycle.viewModelScope
import com.sata.satadelivery.helper.BaseViewModel
import com.sata.satadelivery.repository.DataRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryOrderViewmodel @Inject constructor(private val DateRepoCompnay: DataRepo) :
    BaseViewModel<MainViewState>() {

    val intents : Channel<MainIntent> = Channel<MainIntent>(Channel.UNLIMITED)

    protected val uiState : MutableStateFlow<MainViewState?> = MutableStateFlow(MainViewState())

    val state: MutableStateFlow<MainViewState?> get() = uiState

    private var job: Job? = null



    init {

        getIntent()
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
    override fun onCleared() {

        super.onCleared()
        job!!.cancel()

    }
}
