package com.codesroots.satavendor.presentation.deliveries_fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.codesroots.satavendor.R
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import javax.inject.Inject
import android.view.Gravity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.codesroots.satavendor.databinding.DeliveriesFragmentBinding
import com.codesroots.satavendor.databinding.DeliveryLoginFragmentBinding
import com.codesroots.satavendor.databinding.DetailsOrderFragmentBinding
import com.codesroots.satavendor.helper.WARN_MotionToast
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.Delivery
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.codesroots.satavendor.presentation.details_order_fragment.adapter.DetailsOrderAdapter
import com.codesroots.satavendor.presentation.history_order_fragment.mvi.HistoryOrderViewmodel


class DeliveriesFragment @Inject constructor() : DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by viewModels<CurrentOrderViewModel> { viewModelFactory }

    lateinit var deliveriesAdapter: DeliveriesAdapter

    lateinit var view: DeliveriesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        view = DataBindingUtil.inflate(inflater,
            R.layout.deliveries_fragment, container, false)
        view.listener = ClickHandler()
        view.context = context as MapActivity


        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true);

        dailyOrderRecycleView()

        val data = DeliveryItem(branch_id = 5)
        viewModel.getDeliveris(data)

        viewModel.deliveriesDataLD.observe(requireActivity(),{
          if (it!=null){
              deliveriesAdapter = DeliveriesAdapter(requireContext(),it)
              deliveriesAdapter.data = it
              view.deliveriesRecycle.apply {
                  adapter = deliveriesAdapter
                  isNestedScrollingEnabled = false
                  setHasFixedSize(true)
                  view.progress.isVisible=false
              }
          }else
          {
              WARN_MotionToast("",requireActivity())
          }
        })


        view.dismissBtn.setOnClickListener {
            this.dismiss()
        }

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

    }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = 1800
        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    }


}