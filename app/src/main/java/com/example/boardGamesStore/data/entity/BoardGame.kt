package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "board_games")
data class BoardGame(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)