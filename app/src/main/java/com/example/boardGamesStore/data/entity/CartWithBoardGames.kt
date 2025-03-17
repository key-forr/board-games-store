// data/entity/CartWithBoardGames.kt
package com.example.boardGamesStore.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CartWithBoardGames(
    @Embedded val cartItem: CartItem,
    @Relation(
        parentColumn = "board_game_id",
        entityColumn = "id"
    )
    val boardGame: BoardGame
)