package com.codesroots.satavendor.presentation.new_order_bottomfragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.NeworderFragmentBinding
import com.codesroots.satavendor.helper.*
import com.codesroots.satavendor.models.current_orders.OrderStatus
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.presentation.current_item.CurrentItemFragment
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.MainIntent
import com.codesroots.satavendor.presentation.details_order_fragment.DetailsOrderFragment
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import com.codesroots.satavendor.presentation.order_times_fragment.OrderTimesDialog
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.delivery_login_fragment.*
import org.json.JSONObject
import javax.inject.Inject

class NewOrderFragment @Inject constructor(
    var item: OrdersItem,
    var viewModel: CurrentOrderViewModel,
) : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var mSocket: Socket? = null
    lateinit var orderTimesDialog: OrderTimesDialog



    @Inject
    lateinit var pref: PreferenceHelper
    lateinit var view: NeworderFragmentBinding
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
            R.layout.neworder_fragment, container, false)
        //   view.listener = ClickHandler()
        view.context = context as MapActivity

        Log.d("TAG", "onCreateViewHolder: new")
        view.data = item

        view.listener = ClickHandler()


        try {
            var lat = item.billing_address!!.latitude
            var long = item.billing_address!!.longitude
            viewModel.intents.trySend(MainIntent.getLatLong(viewModel.state.value!!.copy(
                cliendLatitude = lat,
                cliendLongitude = long,
                progress = true)))

        } catch (e: Exception) {

        }
        ////////////// Socket ///////////////////////
        val app: BaseApplication = (context as MapActivity).application as BaseApplication
        mSocket = app.getMSocket()
        mSocket?.connect()
        val options = IO.Options()
        options.reconnection = true //reconnection
        options.forceNew = true

        view.confirmButton.setOnClickListener {
            showOrderTimesDialog(context as MapActivity)

            //connecting socket
//            val confirmData = JSONObject()
//            confirmData.put("roomID", pref.room_id)
//            confirmData.put("delivery_information", 1)
//
//            mSocket?.emit("OrderDeliveryCanceled", confirmData)

            dismiss()

        }

        view.cancelButton.setOnClickListener {
//            val cancelData = JSONObject()
//            cancelData.put("roomID", pref.room_id)
//            cancelData.put("delivery_information", 0)
//
//            mSocket?.emit("OrderDeliveryCanceled", cancelData)

            Error_MotionToast(requireActivity().getString(R.string.cancel2),
                requireActivity())
            cancelRequest()
            dismiss()

        }



        view.detailsButton.setOnClickListener {
            //  this.dismiss()
            ClickHandler().openDialogFragment(requireContext(),
                DetailsOrderFragment(item), "")
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

    fun cancelRequest() {
        val cancelInfo = OrdersItem(
            order_status_id = 5 , order_id = item.order_details?.get(0)?.orderId!!)
        viewModel.deliversOrdersCanceled(cancelInfo)
    }

    private fun showOrderTimesDialog(context: Context) {
        orderTimesDialog = OrderTimesDialog.getInstance(requireContext()){ time ->
            val changeStatusInfo = OrderStatus(
                order_status_id = 1,orderId = item.order_details?.get(0)?.orderId!!, time = time)

            viewModel.changeOrderStatus(item.id!!,changeStatusInfo)

            orderTimesDialog.dismiss()
            (context as MapActivity).let { ClickHandler().openDialogFragment(it, CurrentItemFragment(item), "") }
        }

        orderTimesDialog.show()
    }

}