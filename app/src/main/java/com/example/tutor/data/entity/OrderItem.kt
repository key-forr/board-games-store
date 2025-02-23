package com.example.tutor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "OrderItems",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["id"], childColumns
        = ["order_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Game::class, parentColumns = ["gameId"], childColumns
        = ["game_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val orderItemId: Long? = null,
    @ColumnInfo(name = "order_id") val orderId: Long,
    @ColumnInfo(name = "game_id") val gameId: Int,
    val quantity: Int,
    @ColumnInfo(name = "price") val price: Double
)