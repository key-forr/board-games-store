// data/dao/CartItemDao.kt
package com.example.boardGamesStore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.boardGamesStore.data.entity.CartItem
import com.example.boardGamesStore.data.entity.CartWithBoardGames

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items WHERE user_id = :userId")
    fun getCartItemsByUserId(userId: Long): LiveData<List<CartItem>>

    @Transaction
    @Query("SELECT * FROM cart_items WHERE user_id = :userId")
    fun getCartWithBoardGames(userId: Long): LiveData<List<CartWithBoardGames>>

    @Query("SELECT * FROM cart_items WHERE user_id = :userId AND board_game_id = :boardGameId")
    suspend fun getCartItem(userId: Long, boardGameId: Long): CartItem?

    @Query("SELECT SUM(quantity) FROM cart_items WHERE user_id = :userId")
    fun getCartItemsCount(userId: Long): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE user_id = :userId")
    suspend fun clearCart(userId: Long)
}