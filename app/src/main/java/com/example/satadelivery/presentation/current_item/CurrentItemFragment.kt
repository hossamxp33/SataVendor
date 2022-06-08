package com.example.satadelivery.presentation.current_item

import android.app.Dialog
import android.content.Intent
import android.net.Uri
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
import com.example.satadelivery.helper.PreferenceHelper
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.example.satadelivery.presentation.details_order_fragment.DetailsOrderFragment
import com.example.satadelivery.presentation.map_activity.MapActivity

import javax.inject.Inject

class CurrentItemFragment @Inject constructor(var item: OrdersItem) : DialogFragment() {

    companion object { const val TAG = "TownBottomSheetDialogFragment" }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by viewModels<CurrentOrderViewModel> { viewModelFactory }


    @Inject
    lateinit var pref: PreferenceHelper

    lateinit var view: CurrentItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        setStyle(STYLE_NO_FRAME, R.style.colorPickerStyle)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        view = DataBindingUtil.inflate(inflater,
            R.layout.current_item, container, false)

     //   view.listener = ClickHandler()
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true)

       view.data = item
       view.listener= ClickHandler()
        view.context = context as MapActivity

        val end_latitude = item.billing_address!!.latitude
        val end_longitude =  item.billing_address!!.longitude


        view.confirmButton.setOnClickListener {
            viewModel.changeOrderStatus(item.order_details?.get(0)?.orderId!!,4,item.delivery_id!!)
            view.mView.visibility = View.GONE

        }


        view.detailsButton.setOnClickListener {
            this.dismiss()
            ClickHandler().openDialogFragment(requireContext(),DetailsOrderFragment(item.order_details!!),"")
        }
        view.googleMapsBtn.setOnClickListener {
            val uri =
                "http://maps.google.com/maps?saddr=" + pref.latitude.toString() + "," + pref.longitude.toString() + "&daddr=" + end_latitude.toString() + "," + end_longitude
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
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




}