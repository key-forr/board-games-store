package com.example.boardGamesStore.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.databinding.ItemAdminGameBinding

class AdminGamesAdapter(
    private val onEditClick: (BoardGame) -> Unit,
    private val onDeleteClick: (BoardGame) -> Unit
) : ListAdapter<BoardGame, AdminGamesAdapter.GameViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemAdminGameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GameViewHolder(private val binding: ItemAdminGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: BoardGame) {
            binding.apply {
                gameTitleTv.text = game.name
                gamePriceTv.text = "${game.price} ₴"
                gameStockTv.text = "В наявності"

                // Load image if available
                if (!game.imageUrl.isNullOrEmpty()) {
                    Glide.with(root.context)
                        .load(game.imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(gameImageView)
                } else {
                    gameImageView.setImageResource(R.drawable.placeholder_image)
                }

                editBtn.setOnClickListener { onEditClick(game) }
                deleteBtn.setOnClickListener { onDeleteClick(game) }
            }
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<BoardGame>() {
        override fun areItemsTheSame(oldItem: BoardGame, newItem: BoardGame): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BoardGame, newItem: BoardGame): Boolean {
            return oldItem == newItem
        }
    }
}