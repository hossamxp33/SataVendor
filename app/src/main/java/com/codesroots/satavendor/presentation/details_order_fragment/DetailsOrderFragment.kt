package com.codesroots.satavendor.presentation.details_order_fragment

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.codesroots.satavendor.R
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import javax.inject.Inject
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.codesroots.satavendor.databinding.DetailsOrderFragmentBinding
import com.codesroots.satavendor.helper.Error_MotionToast
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.presentation.details_order_fragment.adapter.DetailsOrderAdapter


class DetailsOrderFragment @Inject constructor(var detailsOrderItems: OrdersItem) :
    DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

    var totalPrice = 0
    var orderPriceValue = 0
    var totalDeliveryCost = 0
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
        view.data = detailsOrderItems.order_details!![0]
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true);

        dailyOrderRecycleView()

        detailsOrderAdapter.submitList(detailsOrderItems.order_details)
        try {
            for (element in detailsOrderItems.order_details!!) {

                orderPriceValue += element.total!!.toInt()

                totalDeliveryCost = detailsOrderItems.delivery_serivce!!.toInt()

            }
        } catch (e: Exception) {
         Error_MotionToast(e.message.toString(),requireActivity())
        }


        view.deliveryTotal.text = totalDeliveryCost.toString()

        view.orderPriceValue.text = orderPriceValue.toString()

        view.total.text = (orderPriceValue + totalDeliveryCost).toString()

        view.closeButton.setOnClickListener {
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


        val displayMetrics = DisplayMetrics()
        val windowsManager =
            context!!.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        params.height = (displayMetrics.heightPixels / 4) * 3
        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    }



}