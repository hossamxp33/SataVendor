package com.example.satadelivery.presentation.daily_order_fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.satadelivery.R
import com.example.satadelivery.databinding.CurrentOrderFragmentBinding
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.helper.UserError
import com.example.satadelivery.presentation.daily_order_fragment.mvi.MainIntent
import com.example.satadelivery.presentation.map_activity.MapActivity
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import android.view.Gravity
import com.example.satadelivery.databinding.DailyOrdersFragmentBinding
import com.example.satadelivery.presentation.daily_order_fragment.adapter.DailyOrderAdapter
import com.example.satadelivery.presentation.daily_order_fragment.mvi.DailyOrderViewModel


class DailyOrderFragment @Inject constructor() : DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<DailyOrderViewModel> { viewModelFactory }
    lateinit var dailyOrderAdapter: DailyOrderAdapter

    lateinit var view: DailyOrdersFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = DataBindingUtil.inflate(inflater,
            R.layout.daily_orders_fragment, container, false)
        view.listener = ClickHandler()
        view.context = context as MapActivity
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true);
        viewModel.intents.trySend(MainIntent.Initialize(viewModel.state.value!!))
        getAllData()
        dailyOrderRecycleView()
        return view.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (savedInstanceState == null) {
            dialog.window?.setWindowAnimations(
                R.style.Animation_Design_BottomSheetDialog
            )
        } else {
            dialog.window?.setWindowAnimations(
                R.style.Animation_Design_BottomSheetDialog
            )
        }

        return dialog
    }


    fun dailyOrderRecycleView() {
        dailyOrderAdapter = DailyOrderAdapter(viewModel.intents, requireContext())
        view.dailyOrderRecycle.apply {
            adapter = dailyOrderAdapter
            isNestedScrollingEnabled = false
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
                            view.progress.isVisible =  true
                        } else {
                            view.progress.visibility = View.GONE
                            if (it.data != null ) {
                                dailyOrderAdapter.submitList(it.data)

//                                var mp = MediaPlayer.create(requireContext(), R.raw.alarm);
//                                mp.start();

                            }else{

                                view.progress.isVisible =  true

                                viewModel.intents.send(MainIntent.Initialize(it))

                            }

                        }

                    }

                }

            }
        }
    }




    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = 1800
        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    }





}