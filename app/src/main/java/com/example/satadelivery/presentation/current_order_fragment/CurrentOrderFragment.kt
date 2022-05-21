package com.example.satadelivery.presentation.current_order_fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.satadelivery.MainActivity
import com.example.satadelivery.R
import com.example.satadelivery.databinding.CurrentOrderFragmentBinding
import com.example.satadelivery.databinding.DailyOrdersFragmentBinding
import com.example.satadelivery.databinding.HistoryOrdersFragmentBinding
import com.example.satadelivery.databinding.NeworderFragmentBinding
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.helper.UserError
import com.example.satadelivery.presentation.current_order_fragment.adapter.CurrentOrdersAdapter
import com.example.satadelivery.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainIntent
import com.example.satadelivery.presentation.map_activity.MapActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class CurrentOrderFragment @Inject constructor() : DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<CurrentOrderViewModel> { viewModelFactory }
    lateinit var currentOrdersAdapter: CurrentOrdersAdapter

    lateinit var view: CurrentOrderFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = DataBindingUtil.inflate(inflater,
            R.layout.current_order_fragment, container, false)
        view.listener = ClickHandler()
        view.context = context as MapActivity

        viewModel.intents.trySend(MainIntent.Initialize(viewModel.state.value!!))
        getAllData()
        currentOrderRecycleView()
        return view.root
    }
    fun currentOrderRecycleView() {
        currentOrdersAdapter = CurrentOrdersAdapter(viewModel.intents, requireContext())
        view.dailyOrderRecycle.apply {
            adapter = currentOrdersAdapter;
            setNestedScrollingEnabled(false)
            setHasFixedSize(true)
        }
    }
    private fun getAllData() {
        lifecycleScope.launchWhenStarted {

            viewModel.state.collect {
                if (it != null) {
                    if (it.error != null) {
                        it.error?.let {
                            when (it) {
                                is UserError.InvalidId -> "Invalid id"
                                is UserError.NetworkError -> it.throwable.message
                                UserError.ServerError -> "Server error"
                                UserError.Unexpected -> "Unexpected error"
                                is UserError.UserNotFound -> "User not found"
                                UserError.ValidationFailed -> "Validation failed"
                            }
                        }
                        viewModel.intents.send(MainIntent.ErrorDisplayed(it))
                    } else {
                        if (it.progress == true) {
                            //  viewModel.intents.send(MainIntent.Initialize(it))
                        } else {
                            view.progress.visibility = View.GONE
                            if (it.data != null ) {
                                currentOrdersAdapter.submitList(it.data)

//                                var mp = MediaPlayer.create(requireContext(), R.raw.alarm);
//                                mp.start();

                            }else{

                            }


                        }
                    }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        setWindowParams()
    }

    private fun setWindowParams() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }


}