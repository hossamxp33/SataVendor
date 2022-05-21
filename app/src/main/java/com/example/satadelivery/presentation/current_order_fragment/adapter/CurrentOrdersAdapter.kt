package com.example.satadelivery.presentation.current_order_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.satadelivery.R
import com.example.satadelivery.databinding.DailyOrdersAdapterBinding
import com.example.satadelivery.databinding.DailyOrdersFragmentBinding
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.models.delivery_orders.DeliveryOrdersItem
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainIntent
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainViewState
import com.example.satadelivery.presentation.map_activity.MapActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow

class CurrentOrdersAdapter (var Intent: Channel<MainIntent>, var context:Context)
    : ListAdapter<DeliveryOrdersItem, MenuViewHolder>(MenuDiffCallback()) {
    lateinit var viewModel: MutableStateFlow<MainViewState?>
    val scope = CoroutineScope(Dispatchers.Main)

    override  fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(context,Intent, currentList[position])

    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
        val binding: DailyOrdersAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context),
            R.layout.daily_orders_adapter, p0, false
        )


        return MenuViewHolder(binding)
    }
}

private  class MenuDiffCallback : DiffUtil.ItemCallback<DeliveryOrdersItem>() {

    override fun areItemsTheSame(oldItem: DeliveryOrdersItem, newItem: DeliveryOrdersItem) =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DeliveryOrdersItem, newItem: DeliveryOrdersItem) =
        oldItem == newItem
}

class MenuViewHolder(
    val binding: DailyOrdersAdapterBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(context: Context?, viewModel: Channel<MainIntent>, data: DeliveryOrdersItem) {

        binding.listener = ClickHandler()
        binding.data = data
        binding.context = context as MapActivity?
    }


}
