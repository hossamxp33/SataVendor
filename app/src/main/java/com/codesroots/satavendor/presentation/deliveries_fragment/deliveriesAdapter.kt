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
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.models.current_orders.OrderDetail
import com.codesroots.satavendor.models.delivery.DeliveryItem
import com.codesroots.satavendor.presentation.map_activity.MapActivity



    class DeliveriesAdapter(
        var context: Context,
        var data: ArrayList<DeliveryItem>,
    ) : RecyclerView.Adapter<CustomViewHolders>() {


        override fun getItemCount(): Int {
            return data.size
        }


        override fun onBindViewHolder(p0: CustomViewHolders, position: Int) {
            p0.bind(data[position], context)
            try {

            }catch (e:Exception){}
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolders {
            val binding: DeliveryItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                    R.layout.delivery_item, p0, false)


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


