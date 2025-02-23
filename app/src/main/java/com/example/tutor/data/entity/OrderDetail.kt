package com.example.tutor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "OrderDetails",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["orderId"], childColumns
        = ["orderId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Game::class, parentColumns = ["gameId"], childColumns
        = ["gameId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class OrderDetail(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "order_id") val orderId: Int,
    @ColumnInfo(name = "game_id") val gameId: Int,
    val quantity: Int,
    val price: Double,
    @ColumnInfo(name = "created_at") val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at") val updatedAt: Date? = null
)