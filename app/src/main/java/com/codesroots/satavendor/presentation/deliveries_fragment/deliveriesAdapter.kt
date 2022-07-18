package com.codesroots.satavendor.presentation.deliveries_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
import com.codesroots.satavendor.presentation.new_order_bottomfragment.NewOrderFragment
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DeliveriesAdapter(
    var context: Context,
    var data: ArrayList<DeliveryItem>,
    var frag: DeliveriesFragment,
) : RecyclerView.Adapter<DeliveriesAdapter.CustomViewHolders>() {
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

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolders {
        val binding: DeliveryItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.delivery_item, p0, false
            )


        return CustomViewHolders(binding)
    }

    inner class CustomViewHolders(
        var binding: DeliveryItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var itemClickable = true

        init {
            binding.mView.setOnClickListener {
                if(itemClickable) {
                    val socketObject = SetorderToDelivery(data[adapterPosition].room_id, order = frag.data)
                    val gson = Gson()
                    val type = object : TypeToken<SetorderToDelivery?>() {}.type
                    val newdata = gson.toJson(socketObject, type)

                    (context as MapActivity).mSocket?.emit("CreateDeliveryRoom", data[adapterPosition].room_id)
                    (context as MapActivity).mSocket?.emit("CreateDeliveryOrder", newdata)
                    itemClickable = false

                    Log.d("TAG","socket// $newdata")
                }
            }

            mSocket?.on("orderDelivery") {
                itemClickable = true
                frag.dismiss()
                val gson = Gson()
                var json = it.first().toString()

//                val type = object : TypeToken<OrdersItem?>() {}.type
//                var newitem = gson.fromJson<OrdersItem>(json, type)
//
                Log.d("TAG","socket// orderDelivery " + json)
//                Toast.makeText(context, "", Toast.LENGTH_LONG)
            }

            mSocket?.on("OrderCanceled") {
//
                val gson = Gson()
                var json = it.first().toString()
//                Toast.makeText(context, "", Toast.LENGTH_LONG)
                Log.d("TAG","socket// orderCanceled " + json)
            }
        }

        fun bind(data: DeliveryItem, context: Context?) {
            binding.data = data
            binding.listener = ClickHandler()
            binding.context = context as MapActivity

        }


    }

}


