package com.example.boardGamesStore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardGamesStore.data.entity.OrderItemWithGame
import com.example.boardGamesStore.databinding.ItemOrderItemBinding

class OrderItemsAdapter : ListAdapter<OrderItemWithGame, OrderItemsAdapter.OrderItemViewHolder>(OrderItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = ItemOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderItemViewHolder(private val binding: ItemOrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItemWithGame: OrderItemWithGame) {
            val orderItem = orderItemWithGame.orderItem
            val boardGame = orderItemWithGame.boardGame

            binding.gameNameTv.text = boardGame.name
            binding.quantityTv.text = "${orderItem.quantity} шт."
            binding.pricePerUnitTv.text = String.format("%.2f грн", orderItem.pricePerUnit)
            binding.totalItemPriceTv.text = String.format("%.2f грн", orderItem.quantity * orderItem.pricePerUnit)

            // Завантаження зображення гри
            Glide.with(binding.root.context)
                .load(boardGame.imageUrl)
                .into(binding.gameImageView)
        }
    }

    class OrderItemDiffCallback : DiffUtil.ItemCallback<OrderItemWithGame>() {
        override fun areItemsTheSame(oldItem: OrderItemWithGame, newItem: OrderItemWithGame): Boolean {
            return oldItem.orderItem.id == newItem.orderItem.id
        }

        override fun areContentsTheSame(oldItem: OrderItemWithGame, newItem: OrderItemWithGame): Boolean {
            return oldItem.orderItem == newItem.orderItem && oldItem.boardGame == newItem.boardGame
        }
    }
}