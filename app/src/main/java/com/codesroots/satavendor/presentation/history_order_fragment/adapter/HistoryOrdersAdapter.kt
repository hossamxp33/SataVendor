package com.codesroots.satavendor.presentation.history_order_fragment.adapter
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
import com.codesroots.satavendor.databinding.HistoryOrderAdapterBinding
import com.codesroots.satavendor.helper.ClickHandler
import com.codesroots.satavendor.models.current_orders.OrdersItem
import com.codesroots.satavendor.presentation.current_item.CurrentItemFragment
import com.codesroots.satavendor.presentation.map_activity.MapActivity


class HistoryOrdersAdapter (var context:Context, var fragment: DialogFragment,
)
    : ListAdapter<OrdersItem, MenuViewHolder>(MenuDiffCallback()) {


    override  fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(context, currentList[position])

    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
        val binding: HistoryOrderAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(p0.context),
            R.layout.history_order_adapter, p0, false
        )

        binding.mView.setOnClickListener {
            fragment.dismiss()
            ClickHandler().openDialogFragment(context, CurrentItemFragment(currentList[p1]),"")
        }
        return MenuViewHolder(binding)
    }
}

private  class MenuDiffCallback : DiffUtil.ItemCallback<OrdersItem>() {

    override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem) =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: OrdersItem, newItem: OrdersItem) =
        oldItem == newItem
}

class MenuViewHolder(
    val binding: HistoryOrderAdapterBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(context: Context?, data: OrdersItem) {

        binding.listener = ClickHandler()
        binding.data = data
        binding.context = context as MapActivity?
    }


}
