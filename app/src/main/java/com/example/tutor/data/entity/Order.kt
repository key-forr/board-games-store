package com.example.tutor.data.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns
        = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = OrderStatus::class, parentColumns
        = ["id"], childColumns = ["status_id"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index(value = ["user_id"]), Index(value = ["status_id"])]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "total_price")
    val totalPrice: Double,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "status_id")
    val statusId: Long?,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)

