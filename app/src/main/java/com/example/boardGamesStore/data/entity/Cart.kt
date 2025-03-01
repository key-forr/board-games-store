package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "Carts",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns
    = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)]
)
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)
