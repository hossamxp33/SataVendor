package com.codesroots.satavendor.presentation.deliveries_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.DeliveryItemBinding
import com.codesroots.satavendor.databinding.DetailsOrderAdapterBinding
import com.codesroots.satavendor.helper.BaseApplication
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.helper.Error_MotionToast
import com.codesroots.satavendor.helper.SUCCESS_MotionToast
import com.codesroots.satavendor.models.auth.Driver
import com.codesroots.satavendor.models.auth.User
import com.codesroots.satavendor.models.current_orders.OrderDetail
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.models.current_orders.SetorderToDelivery
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.presentation.map_activity.MapActivity
import com.codesroots.satavendor.presentation.new_order_bottomfragment.NewOrderFragment
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.delivery_login_fragment.*
import org.json.JSONObject
import org.junit.Assert


class DeliveriesAdapter(
    var context: Context,
    var data: ArrayList<DeliveryItem>,
    var frag: DeliveriesFragment,
) : RecyclerView.Adapter<DeliveriesAdapter.CustomViewHolders>() {


    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(p0: CustomViewHolders, position: Int) {
        p0.bind(data[position], context)
        ////////////// Socket ///////////////////////


        p0.binding.mView.setOnClickListener {
//                if(itemClickable) {

            val gson = Gson()
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val socketObject = SetorderToDelivery(frag.data,"sata123")
            val jsonTut: String = gson.toJson(socketObject)
            println(jsonTut)
            val jsonTutPretty: String = gsonPretty.toJson(socketObject)

            println(jsonTutPretty)

            (context as MapActivity).mSocket?.emit("CreateDeliveryOrder", jsonTutPretty)
            (context as MapActivity).mSocket?.emit("CreateDeliveryRoom", "sata123")

            Log.d("TAG", "socket// $jsonTutPretty")

            frag.view.progress.isVisible = true


        }

        (context as MapActivity).mSocket?.on("OrderCanceled") {
                 val gson = Gson()
                 var json = it.first().toString()

                val type = object : TypeToken<Driver?>() {}.type
                var newitem = gson.fromJson<Driver>(json, type)
            if (newitem.delivery_information == 1) {
                Handler(Looper.getMainLooper()).post {
                    Log.d("TAG", "socket// orderDelivery " + json)
                    SUCCESS_MotionToast("تم قبول الاوردر", context as MapActivity)
                    frag.view.progress.isVisible = false
                    frag.dismiss()
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Error_MotionToast(" تم رفض الاوردر اختار مندوب  أخر ", context as MapActivity)

                }
                Log.d("TAG", "socket// orderCanceled " + json)
            }
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

    inner class CustomViewHolders(
        var binding: DeliveryItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(data: DeliveryItem, context: Context?) {
            binding.data = data
            binding.listener = ClickHandler()
            binding.context = context as MapActivity

        }


    }

}


