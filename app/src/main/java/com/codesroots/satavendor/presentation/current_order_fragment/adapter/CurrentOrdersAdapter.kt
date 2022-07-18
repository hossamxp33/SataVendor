package com.codesroots.satavendor.presentation.current_order_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
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
    var viewModel: CurrentOrderViewModel
    ) : ListAdapter<OrdersItem, CurrentOrdersAdapter.MenuViewHolder>(MenuDiffCallback()) {


    val scope = CoroutineScope(Dispatchers.Main)

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(context, Intent, currentList[position])

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
        val binding: CurrentOrdersAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context),
            R.layout.current_orders_adapter, p0, false
        )

        return MenuViewHolder(binding)
    }


    inner class MenuViewHolder(
        val binding: CurrentOrdersAdapterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.rowCurrentOrder.setOnClickListener {
                Log.d("TAG", "onCreateViewHolder: $adapterPosition " + currentList[adapterPosition].order_status_id)
                when (currentList[adapterPosition].order_status_id) {
                    0 -> {
                        Log.d("TAG", "onCreateViewHolder: $adapterPosition / 0 " + currentList[adapterPosition].order_status_id)
                        ClickHandler().openDialogFragment(
                            context,
                            NewOrderFragment(currentList[adapterPosition], viewModel),
                            ""
                        )
                    }
                    1 -> {
                        Log.d("TAG", "onCreateViewHolder: $adapterPosition / 1 " + currentList[adapterPosition].order_status_id)

                        ClickHandler().openDialogFragment(
                            context,
                            CurrentItemFragment(currentList[adapterPosition]),
                            ""
                        )
                    }

                    2 -> {
                        Log.d("TAG", "onCreateViewHolder: $adapterPosition / 2 " + currentList[adapterPosition].order_status_id)
                        val fm = (context as MapActivity).supportFragmentManager.beginTransaction()
                        val dialogFragment = DeliveriesFragment() // my custom FargmentDialog
                        var args = Bundle()
                        args.putSerializable("item_data", currentList[adapterPosition]);
                        dialogFragment.arguments = args
                        dialogFragment.show(fm, "")
                    }

                    3 -> {
                        Log.d("TAG", "onCreateViewHolder: $adapterPosition / 3 " + currentList[adapterPosition].order_status_id)
                        ClickHandler().openDialogFragment(
                            context,
                            NewOrderFragment(currentList[adapterPosition], viewModel),
                            ""
                        )
                    }
                    else -> {
                        Log.d("TAG", "onCreateViewHolder: $adapterPosition / else " + currentList[adapterPosition].order_status_id)
                        ClickHandler().openDialogFragment(
                            context,
                            NewOrderFragment(currentList[adapterPosition], viewModel),
                            ""
                        )
                    }


                }


                fragment.dismiss()

//            ClickHandler().openDialogFragment(context, CurrentItemFragment(currentList[p1]), "")
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

        fun bind(
            context: Context?,
            viewModel: Channel<MainIntent>,
            data: OrdersItem,
        ) {

            binding.listener = ClickHandler()
            binding.data = data
            binding.context = context as MapActivity?
            binding.fragment = CurrentItemFragment(data)


            val status = when (data.order_status_id) {
                0 -> "New Order"
                1 -> "Preparing"
                2 -> "Prepared"
                3 -> "Done"
                else -> "Tracking"
            }
            binding.status = status

        }


    }
}


private class MenuDiffCallback : DiffUtil.ItemCallback<OrdersItem>() {

    override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem) =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: OrdersItem, newItem: OrdersItem) =
        oldItem == newItem
}

