package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BoardGame::class,
            parentColumns = ["id"],
            childColumns = ["board_game_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("order_id"),
        Index("board_game_id")
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "order_id")
    val orderId: Long,

    @ColumnInfo(name = "board_game_id")
    val boardGameId: Long,

    val quantity: Int,

    @ColumnInfo(name = "price_per_unit")
    val pricePerUnit: Double
)