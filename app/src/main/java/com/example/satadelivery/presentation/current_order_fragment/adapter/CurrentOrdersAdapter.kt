package com.example.satadelivery.presentation.current_order_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.satadelivery.R
import com.example.satadelivery.databinding.CurrentOrdersAdapterBinding
import com.example.satadelivery.helper.ClickHandler
import com.example.satadelivery.models.current_orders.OrdersItem
import com.example.satadelivery.presentation.current_item.CurrentItemFragment
import com.example.satadelivery.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainIntent
import com.example.satadelivery.presentation.current_order_fragment.mvi.MainViewState

import com.example.satadelivery.presentation.map_activity.MapActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class CurrentOrdersAdapter(
    var Intent: Channel<MainIntent>,
    var context: Context,
    var fragment: DialogFragment,
    var viewModel: CurrentOrderViewModel,

    ) : ListAdapter<OrdersItem, MenuViewHolder>(MenuDiffCallback()) {


    val scope = CoroutineScope(Dispatchers.Main)

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(context, Intent, currentList[position])

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
        val binding: CurrentOrdersAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context),
            R.layout.current_orders_adapter, p0, false
        )

        binding.mView.setOnClickListener {
            fragment.dismiss()
            ClickHandler().openDialogFragment(context, CurrentItemFragment(currentList[p1]), "")

            val lat = currentList[0].billing_address.latitude
            val long = currentList[0].billing_address.longitude

            viewModel.getLatLong(lat, long)
            viewModel.intents.trySend(MainIntent.getLatLong(viewModel.state.value!!.copy(cliendLatitude = lat,cliendLongitude = long,progress = true)))

        }
        return MenuViewHolder(binding)
    }
}

private class MenuDiffCallback : DiffUtil.ItemCallback<OrdersItem>() {

    override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem) =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: OrdersItem, newItem: OrdersItem) =
        oldItem == newItem
}

class MenuViewHolder(
    val binding: CurrentOrdersAdapterBinding,
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(
        context: Context?,
        viewModel: Channel<MainIntent>,
        data: OrdersItem,
    ) {

        binding.listener = ClickHandler()
        binding.data = data
        binding.context = context as MapActivity?
        binding.fragment = CurrentItemFragment(data)

    }


}
