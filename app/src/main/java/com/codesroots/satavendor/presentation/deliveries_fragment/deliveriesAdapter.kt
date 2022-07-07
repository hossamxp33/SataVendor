package com.codesroots.satavendor.presentation.deliveries_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.DeliveryItemBinding
import com.codesroots.satavendor.databinding.DetailsOrderAdapterBinding
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.helper.SUCCESS_MotionToast
import com.codesroots.satavendor.models.current_orders.OrderDetail
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.current_orders.SetorderToDelivery
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DeliveriesAdapter(
    var context: Context,
    var data: ArrayList<DeliveryItem>,
    var frag: DeliveriesFragment,
) : RecyclerView.Adapter<CustomViewHolders>() {
    var mSocket: Socket? = null


    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(p0: CustomViewHolders, position: Int) {
        p0.bind(data[position], context)
        ////////////// Socket ///////////////////////
        val app: BaseApplication = (context as MapActivity).application as BaseApplication
        mSocket = app.getMSocket()
        mSocket?.connect()
        val options = IO.Options()
        options.reconnection = true //reconnection
        options.forceNew = true
        try {
            p0.binding.mView.setOnClickListener {

                val socketObject = SetorderToDelivery(data[position].room_id,order = frag.data)
                val gson = Gson()
                val type = object : TypeToken<SetorderToDelivery?>() {}.type
                val newdata = gson.toJson(socketObject, type)

                (context as MapActivity).mSocket?.emit("CreateDeliveryOrder",newdata)

            }
        } catch (e: Exception) {
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolders {
        val binding: DeliveryItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.delivery_item, p0, false
            )


        return CustomViewHolders(binding)
    }


}

class CustomViewHolders(
    var binding: DeliveryItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: DeliveryItem, context: Context?) {
        binding.data = data
        binding.listener = ClickHandler()
        binding.context = context as MapActivity

    }

}


