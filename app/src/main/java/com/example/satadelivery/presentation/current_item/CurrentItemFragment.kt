package com.example.satadelivery.presentation.current_item

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.satadelivery.R
import com.example.satadelivery.databinding.CurrentItemBinding

import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.current_order_fragment.adapter.CurrentOrdersAdapter
import com.example.satadelivery.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainIntent
import com.example.satadelivery.presentation.details_order_fragment.DetailsOrderFragment

import javax.inject.Inject

class CurrentItemFragment @Inject constructor(var item: OrdersItem) : DialogFragment() {

    companion object { const val TAG = "TownBottomSheetDialogFragment" }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<CurrentOrderViewModel> { viewModelFactory }

    lateinit var currentOrdersAdapter: CurrentOrdersAdapter


    lateinit var view: CurrentItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle);

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = DataBindingUtil.inflate(inflater,
            R.layout.current_item, container, false)

     //   view.listener = ClickHandler()
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true);
        viewModel.intents.trySend(MainIntent.Initialize(viewModel.state.value!!))

       view.data = item


        view.detailsButton.setOnClickListener {
            this.dismiss()
            ClickHandler().openDialogFragment(requireContext(),DetailsOrderFragment(item.order_details),"")
        }

        return view.root
    }



    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = 1800
        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    }






}