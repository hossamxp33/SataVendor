package com.codesroots.satavendor.presentation.order_times_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codesroots.satavendor.databinding.RowItemOrderTimeBinding

class OrderTimesAdapter(private val onItemClick: (Int?) -> Unit) :
    ListAdapter<Int, OrderTimesAdapter.OrderTimesViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderTimesViewHolder {
        val binding =
            RowItemOrderTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderTimesViewHolder(binding) {
            onItemClick(it)
        }
    }

    override fun onBindViewHolder(holder: OrderTimesViewHolder, position: Int) {
        val item = currentList[position]
        item?.let {
            holder.bind(it)
        }
    }

    inner class OrderTimesViewHolder(
        private val itemBinding: RowItemOrderTimeBinding,
        onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.cardTime.setOnClickListener {
                onItemClick(currentList[adapterPosition])

            }
        }

        fun bind(item: Int) {
            item.apply {
                itemBinding.tvTime.text = "$this"
            }
        }
    }

}