package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "OrderItems",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns
        = ["id"], childColumns = ["order_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Game::class, parentColumns
        = ["id"], childColumns = ["game_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val quantity: Int,
    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "order_id")
    val orderId: Long,

    @ColumnInfo(name = "game_id")
    val gameId: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)
