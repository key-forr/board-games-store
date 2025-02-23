package com.example.tutor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "Orders",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns
    = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "total_price") val totalPrice: Double,
    val status: String = "pending",
    @ColumnInfo(name = "created_at") val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at") val updatedAt: Date? = null
)