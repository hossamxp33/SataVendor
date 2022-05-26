package com.example.satadelivery.presentation.details_order_fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.example.satadelivery.R
import com.example.satadelivery.helper.BaseApplication
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.presentation.map_activity.MapActivity
import javax.inject.Inject
import android.view.Gravity
import com.example.satadelivery.databinding.DailyOrdersFragmentBinding
import com.example.satadelivery.databinding.DetailsOrderFragmentBinding
import com.example.satadelivery.models.current_orders.OrderDetail
import com.example.satadelivery.presentation.details_order_fragment.adapter.DetailsOrderAdapter


class DetailsOrderFragment @Inject constructor(var detailsOrderItems: List<OrderDetail>) :
    DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }


    lateinit var detailsOrderAdapter: DetailsOrderAdapter

    lateinit var view: DetailsOrderFragmentBinding

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
            R.layout.details_order_fragment, container, false)
        view.listener = ClickHandler()
        view.context = context as MapActivity

        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true);

        dailyOrderRecycleView()

        detailsOrderAdapter.submitList(detailsOrderItems)
        view.closeButton.setOnClickListener {
            this.dismiss()
        }

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
        detailsOrderAdapter = DetailsOrderAdapter(requireContext())
        view.dailyOrderRecycle.apply {
            adapter = detailsOrderAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
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