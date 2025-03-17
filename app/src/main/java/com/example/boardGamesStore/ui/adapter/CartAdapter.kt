// ui/adapter/CartAdapter.kt
package com.example.boardGamesStore.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.entity.CartWithBoardGames

class CartAdapter(
    private val onItemClick: (CartWithBoardGames) -> Unit,
    private val onUpdateQuantity: (Long, Int) -> Unit,
    private val onRemoveItem: (Long) -> Unit
) : ListAdapter<CartWithBoardGames, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_cart_game)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_cart_game_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.tv_cart_price)
        private val quantityTextView: TextView = itemView.findViewById(R.id.tv_cart_quantity)
        private val btnDecrease: ImageButton = itemView.findViewById(R.id.btn_decrease)
        private val btnIncrease: ImageButton = itemView.findViewById(R.id.btn_increase)
        private val btnRemove: ImageButton = itemView.findViewById(R.id.btn_remove)

        fun bind(cartItem: CartWithBoardGames) {
            val boardGame = cartItem.boardGame

            // Встановлення зображення
            boardGame.imageUrl?.let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .placeholder(R.drawable.placeholder_game)
                    .error(R.drawable.error_image)
                    .into(imageView)
            } ?: run {
                imageView.setImageResource(R.drawable.placeholder_game)
            }

            // Встановлення тексту
            nameTextView.text = boardGame.name
            priceTextView.text = String.format("₴%.2f", boardGame.price)
            quantityTextView.text = cartItem.cartItem.quantity.toString()

            // Встановлення слухачів
            itemView.setOnClickListener {
                onItemClick(cartItem)
            }

            btnDecrease.setOnClickListener {
                val currentQuantity = cartItem.cartItem.quantity
                if (currentQuantity > 1) {
                    onUpdateQuantity(boardGame.id!!, currentQuantity - 1)
                }
            }

            btnIncrease.setOnClickListener {
                onUpdateQuantity(boardGame.id!!, cartItem.cartItem.quantity + 1)
            }

            btnRemove.setOnClickListener {
                onRemoveItem(boardGame.id!!)
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartWithBoardGames>() {
        override fun areItemsTheSame(oldItem: CartWithBoardGames, newItem: CartWithBoardGames): Boolean {
            return oldItem.cartItem.boardGameId == newItem.cartItem.boardGameId
        }

        override fun areContentsTheSame(oldItem: CartWithBoardGames, newItem: CartWithBoardGames): Boolean {
            return oldItem.cartItem == newItem.cartItem &&
                    oldItem.boardGame == newItem.boardGame
        }
    }
}