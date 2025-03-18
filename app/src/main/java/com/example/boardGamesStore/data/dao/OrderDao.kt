package com.example.boardGamesStore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.boardGamesStore.data.entity.Order
import com.example.boardGamesStore.data.entity.OrderItem
import com.example.boardGamesStore.data.entity.OrderWithItems
import java.util.Date

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItem): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Long): Order?

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderWithItems(orderId: Long): OrderWithItems?

    @Transaction
    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC")
    fun getOrdersForUser(userId: Long): LiveData<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders ORDER BY created_at DESC")
    fun getAllOrders(): LiveData<List<OrderWithItems>>

    @Query("UPDATE orders SET status = :status, updated_at = :updatedAt WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String, updatedAt: Date)
}