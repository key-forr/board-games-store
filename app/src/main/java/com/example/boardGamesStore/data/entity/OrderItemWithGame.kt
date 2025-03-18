package com.example.boardGamesStore.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class OrderItemWithGame(
    @Embedded val orderItem: OrderItem,
    @Relation(
        parentColumn = "board_game_id",
        entityColumn = "id"
    )
    val boardGame: BoardGame
)