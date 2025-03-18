package com.example.boardGamesStore.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val items: List<OrderItem>
)