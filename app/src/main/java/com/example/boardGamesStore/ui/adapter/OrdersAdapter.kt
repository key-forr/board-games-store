package com.example.boardGamesStore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.boardGamesStore.data.entity.OrderWithItems
import com.example.boardGamesStore.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OrdersAdapter : ListAdapter<OrderWithItems, OrdersAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderWithItems: OrderWithItems) {
            val order = orderWithItems.order
            val items = orderWithItems.items

            binding.orderIdTv.text = "Замовлення #${order.id}"

            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.orderDateTv.text = dateFormat.format(order.createdAt)

            binding.totalPriceTv.text = String.format("%.2f грн", order.totalPrice)
            binding.statusTv.text = getStatusText(order.status)
            binding.itemsCountTv.text = "${items.size} товарів"
        }

        private fun getStatusText(status: String): String {
            return when (status) {
                "PENDING" -> "Очікує обробки"
                "CONFIRMED" -> "Підтверджено"
                "SHIPPED" -> "Відправлено"
                "DELIVERED" -> "Доставлено"
                "CANCELLED" -> "Скасовано"
                else -> status
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<OrderWithItems>() {
        override fun areItemsTheSame(oldItem: OrderWithItems, newItem: OrderWithItems): Boolean {
            return oldItem.order.id == newItem.order.id
        }

        override fun areContentsTheSame(oldItem: OrderWithItems, newItem: OrderWithItems): Boolean {
            return oldItem.order == newItem.order && oldItem.items == newItem.items
        }
    }
}