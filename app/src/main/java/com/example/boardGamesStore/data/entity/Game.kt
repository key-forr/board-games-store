package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Games")
data class Game (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int = 0,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)