// ui/adapter/BoardGameAdapter.kt
package com.example.boardGamesStore.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.entity.BoardGame

class BoardGameAdapter(private val onItemClick: (BoardGame) -> Unit) :
    ListAdapter<BoardGame, BoardGameAdapter.BoardGameViewHolder>(BoardGameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_board_game, parent, false)
        return BoardGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardGameViewHolder, position: Int) {
        val boardGame = getItem(position)
        holder.bind(boardGame)
    }

    inner class BoardGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_board_game)
        private val priceTextView: TextView = itemView.findViewById(R.id.tv_price)

        fun bind(boardGame: BoardGame) {
            // Відображення зображення гри
            boardGame.imageUrl?.let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .placeholder(R.drawable.placeholder_game)
                    .error(R.drawable.error_image)
                    .into(imageView)
            } ?: run {
                imageView.setImageResource(R.drawable.placeholder_game)
            }

            // Відображення ціни
            priceTextView.text = String.format("₴%.2f", boardGame.price)

            // Встановлення слухача на клік
            itemView.setOnClickListener {
                onItemClick(boardGame)
            }
        }
    }

    class BoardGameDiffCallback : DiffUtil.ItemCallback<BoardGame>() {
        override fun areItemsTheSame(oldItem: BoardGame, newItem: BoardGame): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BoardGame, newItem: BoardGame): Boolean {
            return oldItem == newItem
        }
    }
}