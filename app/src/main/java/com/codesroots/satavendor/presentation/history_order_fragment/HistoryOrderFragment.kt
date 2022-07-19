package com.codesroots.satavendor.presentation.history_order_fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.HistoryOrdersFragmentBinding
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.codesroots.satavendor.helper.UserError
import com.codesroots.satavendor.models.current_orders.DateModel
import com.codesroots.satavendor.presentation.history_order_fragment.adapter.HistoryOrdersAdapter
import com.codesroots.satavendor.presentation.history_order_fragment.mvi.HistoryOrderViewmodel
import com.codesroots.satavendor.presentation.history_order_fragment.mvi.MainIntent
import kotlinx.coroutines.flow.collect


class HistoryOrderFragment @Inject constructor() : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<HistoryOrderViewmodel> { viewModelFactory }

    lateinit var historyOrdersAdapter: HistoryOrdersAdapter
    var dateInfo: DateModel? = null

    var sDay = 0
    var sMonth: Int = 0
    var sYear: Int = 0

    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var totalPrice = 0
    var orderPriceValue = 0
    var totalDeliveryCost = 0
    var end: String? = null

    lateinit var view: HistoryOrdersFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = DataBindingUtil.inflate(
            inflater,
            R.layout.history_orders_fragment, container, false
        )
        //   view.listener = ClickHandler()
        view.context = context as MapActivity


        view.startTime.setOnClickListener {
            getCurrentDate()

        }

        view.endTime.setOnClickListener {
            getCurrentDate()
        }

//        view.dismissBtn.setOnClickListener {
//            this.dismiss()
//        }

        view.getData.setOnClickListener {

            viewModel.intents.trySend(MainIntent.Initialize(viewModel.state.value!!, dateInfo))
            view.getData.isVisible = false

        }
        //val dateInfo= DateModel(date_from = "2022-05-19",date_to = "2022-05-19")
        getAllData()

        historyOrderRecycleView()

        return view.root
    }


    fun getCurrentDate() {
        val calendar: Calendar = Calendar.getInstance()
        sDay = calendar.get(Calendar.DAY_OF_MONTH)
        sMonth = calendar.get(Calendar.MONTH)
        sYear = calendar.get(Calendar.YEAR)

        val datePickerDialog =

            DatePickerDialog(
                requireContext(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, sYear, sMonth, sDay
            )

        datePickerDialog.show()

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
                            view.progress.isVisible = true


                        } else {
                            view.progress.visibility = View.GONE
                            if (it.data != null) {
                               view. chooseDate.isVisible = false
                                historyOrdersAdapter.submitList(it.filterData)
                                for (i in 0 until it.data!!.size) {
                                    orderPriceValue += it.data!![i].total!!.toInt()
                                    totalDeliveryCost = it.data!![i].delivery_serivce!!.toInt()
                                }

                                view.deliveryTotal.text = totalDeliveryCost.toString()

                                view.orderPriceValue.text = orderPriceValue.toString()

                                view.total.text = (orderPriceValue + totalDeliveryCost).toString()


                                view.filterLayout.isVisible = true

                                view.delivered.setOnClickListener { it1 ->

                                    noColoredView()
                                    coloredView(it1)

                                    viewModel.intents.trySend(MainIntent.FilterData(viewModel.state.value!!, 4))

                                }

                                view.canceledOrders.setOnClickListener { it1 ->
                                    noColoredView()
                                    coloredView(it1)
                                    viewModel.intents.trySend(
                                        MainIntent.FilterData(
                                            viewModel.state.value!!,
                                            5
                                        )
                                    )


                                }

                                view.issuesOrders.setOnClickListener { it1 ->

                                    noColoredView()
                                    coloredView(it1)
                                    viewModel.intents.trySend(
                                        MainIntent.FilterData(
                                            viewModel.state.value!!,
                                            6
                                        )
                                    )


                                }
                            } else {

                                //  viewModel.intents.trySend(MainIntent.Initialize(viewModel.state.value!!,dateInfo))


                            }

                        }

                    }

                }

            }
        }
    }

    fun historyOrderRecycleView() {
        historyOrdersAdapter = HistoryOrdersAdapter(requireContext(), this)
        view.dailyOrderRecycle.apply {
            adapter = historyOrdersAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        myYear = p1
        myMonth = p2 + 1
        myDay = p3

        when {
            view.startTime.isEnabled -> {
                view.endTime.isEnabled = true
                view.startTime.text = "$myYear-$myMonth-$myDay"
                view.startTime.isEnabled = false
            }
            view.endTime.isEnabled -> {
                view.endTime.text = "$myYear-$myMonth-$myDay"
                view.endTime.isEnabled = false
                view.startTime.isEnabled = true

                dateInfo = DateModel(
                    date_from = view.startTime.text.toString(),
                    date_to = view.endTime.text.toString(),
                    branch_id = (context as MapActivity).Pref.VendorId.toString()
                )

                view.getData.isVisible = true
            }
            else -> {
                view.startTime.isEnabled = true
            }
        }


    }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT

        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

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

    private fun coloredView(view: View) {
        view.setBackgroundColor(
            ContextCompat
                .getColor(requireContext(), R.color.light_orange)
        )
    }

    private fun whiteView(view: View) {
        view.setBackgroundColor(
            ContextCompat
                .getColor(requireContext(), R.color.white)
        )
    }

    private fun noColoredView() {
        whiteView(view.issuesOrders)
        whiteView(view.delivered)
        whiteView(view.canceledOrders)

    }
}