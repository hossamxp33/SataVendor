package com.codesroots.satavendor.presentation.current_item

import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.CurrentItemBinding
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.helper.PreferenceHelper
import com.codesroots.satavendor.models.current_orders.OrderStatus
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.codesroots.satavendor.presentation.deliveries_fragment.DeliveriesFragment
import com.codesroots.satavendor.presentation.details_order_fragment.DetailsOrderFragment
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_dialog.*
import kotlinx.android.synthetic.main.bottom_dialog.call
import kotlinx.android.synthetic.main.bottom_dialog.cancel
import kotlinx.android.synthetic.main.cancel_dialog.*
import javax.inject.Inject


class CurrentItemFragment @Inject constructor(var item: OrdersItem) : DialogFragment() {

    companion object {
        const val TAG = "TownBottomSheetDialogFragment"
    }

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
        view = DataBindingUtil.inflate(
            inflater,
            R.layout.current_item, container, false
        )

        //   view.listener = ClickHandler()
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true)
        view.data = item
        view.listener = ClickHandler()
        view.context = context as MapActivity


        view.confirmButton.setOnClickListener {
            confirmRequest()
//            val fragmentTransaction =
//                (context as MapActivity).supportFragmentManager.beginTransaction()
//            fragmentTransaction.add(DeliveriesFragment(), tag)
//            fragmentTransaction.commitAllowingStateLoss()
            val fm = (context as MapActivity).supportFragmentManager.beginTransaction()
            val dialogFragment = DeliveriesFragment() // my custom FargmentDialog
            var args = Bundle()
            args.putSerializable("item_data", item);
            dialogFragment.arguments = args
            dialogFragment.show(fm, "")
            Log.d("TAG","socket// setOnClickListener data $item")

            view.mView.visibility = View.GONE

        }



        view.dismissBtn.setOnClickListener {
            this.dismiss()
        }

        view.detailsButton.setOnClickListener {
            this.dismiss()
            ClickHandler().openDialogFragment(
                requireContext(),
                DetailsOrderFragment(item),
                ""
            )
        }
        view.googleMapsBtn.setOnClickListener {
            val uri =
                "http://maps.google.com/maps?saddr=" + pref.latitude.toString() + "," + pref.longitude.toString() + "&daddr=" + item.billing_address!!.latitude.toString() + "," + item.billing_address!!.longitude
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }


        return view.root
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        );
        dialog!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = 1100
        dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (savedInstanceState == null) {
            dialog.window?.setWindowAnimations(
                R.style.Animation_Design_BottomSheetDialog
            )

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)


        } else {
            dialog.window?.setWindowAnimations(
                R.style.Animation_Design_BottomSheetDialog
            )
        }


        return dialog
    }


    fun confirmRequest() {
        val changeStatusInfo = OrderStatus(
            order_status_id = 3, id = item.id!!
        )
        viewModel.changeOrderStatus(item.id!!, changeStatusInfo)
    }

}