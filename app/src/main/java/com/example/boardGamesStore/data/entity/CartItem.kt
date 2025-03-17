package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "cart_items",
    primaryKeys = ["user_id", "board_game_id"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BoardGame::class,
            parentColumns = ["id"],
            childColumns = ["board_game_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("user_id"),
        Index("board_game_id")
    ]
)
data class CartItem(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "board_game_id")
    val boardGameId: Long,
    var quantity: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)