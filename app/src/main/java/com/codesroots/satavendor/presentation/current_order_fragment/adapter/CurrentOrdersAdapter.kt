package com.codesroots.satavendor.presentation.current_order_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codesroots.satavendor.R
import com.codesroots.satavendor.databinding.CurrentOrdersAdapterBinding
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.presentation.current_item.CurrentItemFragment
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.CurrentOrderViewModel
import com.codesroots.satavendor.presentation.current_order_fragment.mvi.MainIntent
import com.codesroots.satavendor.presentation.deliveries_fragment.DeliveriesFragment

import com.codesroots.satavendor.presentation.map_activity.MapActivity
import com.codesroots.satavendor.presentation.new_order_bottomfragment.NewOrderFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel

class CurrentOrdersAdapter(
    var Intent: Channel<MainIntent>,
    var context: Context,
    var fragment: DialogFragment,
    var viewModel: CurrentOrderViewModel,

    ) : ListAdapter<OrdersItem, MenuViewHolder>(MenuDiffCallback()) {


    val scope = CoroutineScope(Dispatchers.Main)

    override fun onBindViewHolder(holder: MenuViewHolder, p1: Int) {
        holder.bind(context, Intent, currentList[p1])
        holder.binding.mView.setOnClickListener {
            when (currentList[p1].order_status_id) {
                0 -> {
                    ClickHandler().openDialogFragment(
                        context,
                        NewOrderFragment(currentList[p1], viewModel),
                        ""
                    )}
                1 -> {
                    ClickHandler().openDialogFragment(
                        context,
                        CurrentItemFragment(currentList[p1]),
                        ""
                    )}
                2 -> { ClickHandler().openDialogFragment(
                    (context as MapActivity),
                    DeliveriesFragment(),
                    ""
                )}
                3 -> { ClickHandler().openDialogFragment(
                    context,
                    NewOrderFragment(currentList[p1], viewModel),
                    ""
                )}
                else -> { ClickHandler().openDialogFragment(
                    context,
                    NewOrderFragment(currentList[p1], viewModel),
                    ""
                )


                }
            }


            fragment.dismiss()

            try {
                val lat = currentList[0].billing_address!!.latitude
                val long = currentList[0].billing_address!!.longitude

                viewModel.getLatLong(lat, long)

                viewModel.intents.trySend(
                    MainIntent.getLatLong(
                        viewModel.state.value!!.copy(
                            cliendLatitude = lat,
                            cliendLongitude = long,
                            progress = true
                        )
                    )
                )

            } catch (e: Exception) {
            }

        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
        val binding: CurrentOrdersAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context),
            R.layout.current_orders_adapter, p0, false
        )



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
