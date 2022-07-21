package com.codesroots.satavendor.presentation.history_order_fragment

import android.app.Dialog

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.codesroots.satavendor.R
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.codesroots.satavendor.databinding.DailyOrdersFragmentBinding
import com.codesroots.satavendor.helper.UserError
import com.codesroots.satavendor.models.current_orders.DateModel
import com.codesroots.satavendor.presentation.history_order_fragment.adapter.HistoryOrdersAdapter
import com.codesroots.satavendor.presentation.history_order_fragment.mvi.HistoryOrderViewmodel
import com.codesroots.satavendor.presentation.history_order_fragment.mvi.MainIntent
import kotlinx.coroutines.flow.collect


class DailyOrdersFragment @Inject constructor() : DialogFragment()
  {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<HistoryOrderViewmodel> { viewModelFactory }

    lateinit var historyOrdersAdapter: HistoryOrdersAdapter

    var dateInfo : DateModel ? = null

    var sDay = 0
    var sMonth: Int = 0
    var sYear: Int = 0
      var totalPrice = 0
    var end: String? = null
      var totalDeliveryCost= 0
      var orderPriceValue = 0

    lateinit var view: DailyOrdersFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        view = DataBindingUtil.inflate(inflater,
            R.layout.daily_orders_fragment, container, false)
        //   view.listener = ClickHandler()
        view.context = context as MapActivity

        val calendar: Calendar = Calendar.getInstance()
        sDay = calendar.get(Calendar.DAY_OF_MONTH)
        sMonth = calendar.get(Calendar.MONTH) + 1
        sYear = calendar.get(Calendar.YEAR)


        dateInfo= DateModel(date_from = "$sYear-$sMonth-$sDay",date_to ="$sYear-$sMonth-$sDay", (context as MapActivity).Pref.VendorId.toString())

        viewModel.intents.trySend(MainIntent.Initialize(viewModel.state.value!!,dateInfo))

        getAllData()
        historyOrderRecycleView()
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
                            if (!it.data.isNullOrEmpty() ) {
                                view.noOrderFound.isVisible = false
                                historyOrdersAdapter.submitList(it.data)

                                for (i in 0 until it.data!!.size) {

                                    orderPriceValue += it.data!![i].total!!.toInt()

                                    totalDeliveryCost = it.data!![i].delivery_serivce!!.toInt()

                                }

                                view.deliveryTotal.text = totalDeliveryCost.toString()

                                view.orderPriceValue.text = orderPriceValue.toString()

                                view.total.text = (orderPriceValue + totalDeliveryCost).toString()



//                                var mp = MediaPlayer.create(requireContext(), R.raw.alarm);
//                                mp.start();

                            } else {

                                view.progress.isVisible = true
                                   view.noOrderFound.isVisible = true


                            }

                        }

                    }

                }

            }
        }
    }
    fun historyOrderRecycleView() {
        historyOrdersAdapter = HistoryOrdersAdapter(requireContext(),this)
        view.dailyOrderRecycle.apply {
            adapter = historyOrdersAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }




      override fun onResume() {
          super.onResume()
          val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
          params.width = ViewGroup.LayoutParams.MATCH_PARENT


          val displayMetrics = DisplayMetrics()
          val windowsManager =
              context!!.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
          windowsManager.defaultDisplay.getMetrics(displayMetrics)
          params.height = (displayMetrics.heightPixels / 4) * 3
          dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

      }


}