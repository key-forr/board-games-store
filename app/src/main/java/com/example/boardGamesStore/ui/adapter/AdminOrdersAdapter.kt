// ui/adapter/AdminOrdersAdapter.kt
package com.example.boardGamesStore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.boardGamesStore.data.entity.OrderWithItems
import com.example.boardGamesStore.databinding.ItemAdminOrderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdminOrdersAdapter(
    private val onStatusChange: (orderId: Long, newStatus: String) -> Unit
) : ListAdapter<OrderWithItems, AdminOrdersAdapter.AdminOrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        val binding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AdminOrderViewHolder(private val binding: ItemAdminOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderWithItems: OrderWithItems) {
            val order = orderWithItems.order
            val items = orderWithItems.items

            binding.orderIdTv.text = "Замовлення #${order.id}"

            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.orderDateTv.text = dateFormat.format(order.createdAt)

            binding.totalPriceTv.text = String.format("%.2f грн", order.totalPrice)
            binding.statusTv.text = getStatusText(order.status)
            binding.itemsCountTv.text = "${items.size} товарів"
            binding.addressTv.text = order.deliveryAddress
            binding.phoneTv.text = order.phoneNumber

            binding.confirmBtn.setOnClickListener {
                onStatusChange(order.id!!, "CONFIRMED")
            }

            binding.shipBtn.setOnClickListener {
                onStatusChange(order.id!!, "SHIPPED")
            }

            binding.deliverBtn.setOnClickListener {
                onStatusChange(order.id!!, "DELIVERED")
            }

            binding.cancelBtn.setOnClickListener {
                onStatusChange(order.id!!, "CANCELLED")
            }

            updateButtonState(order.status)
        }

        private fun updateButtonState(status: String) {
            when (status) {
                "PENDING" -> {
                    binding.confirmBtn.isEnabled = true
                    binding.shipBtn.isEnabled = false
                    binding.deliverBtn.isEnabled = false
                    binding.cancelBtn.isEnabled = true
                }
                "CONFIRMED" -> {
                    binding.confirmBtn.isEnabled = false
                    binding.shipBtn.isEnabled = true
                    binding.deliverBtn.isEnabled = false
                    binding.cancelBtn.isEnabled = true
                }
                "SHIPPED" -> {
                    binding.confirmBtn.isEnabled = false
                    binding.shipBtn.isEnabled = false
                    binding.deliverBtn.isEnabled = true
                    binding.cancelBtn.isEnabled = true
                }
                "DELIVERED" -> {
                    binding.confirmBtn.isEnabled = false
                    binding.shipBtn.isEnabled = false
                    binding.deliverBtn.isEnabled = false
                    binding.cancelBtn.isEnabled = false
                }
                "CANCELLED" -> {
                    binding.confirmBtn.isEnabled = false
                    binding.shipBtn.isEnabled = false
                    binding.deliverBtn.isEnabled = false
                    binding.cancelBtn.isEnabled = false
                }
            }
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