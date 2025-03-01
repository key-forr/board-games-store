package com.example.boardGamesStore.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "CartItems",
    foreignKeys = [
        ForeignKey(entity = Cart::class, parentColumns
        = ["id"], childColumns = ["cart_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Game::class, parentColumns
        = ["id"], childColumns = ["game_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "cart_id")
    val cartId: Long,

    @ColumnInfo(name = "game_id")
    val gameId: Long,

    val quantity: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)
