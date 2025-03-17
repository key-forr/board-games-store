// data/repository/CartRepository.kt
package com.example.boardGamesStore.data.repository

import androidx.lifecycle.LiveData
import com.example.boardGamesStore.data.dao.CartItemDao
import com.example.boardGamesStore.data.entity.CartItem
import com.example.boardGamesStore.data.entity.CartWithBoardGames
import java.util.Date

class CartRepository(private val cartItemDao: CartItemDao) {
    fun getCartWithBoardGames(userId: Long): LiveData<List<CartWithBoardGames>> {
        return cartItemDao.getCartWithBoardGames(userId)
    }

    fun getCartItemsCount(userId: Long): LiveData<Int> {
        return cartItemDao.getCartItemsCount(userId)
    }

    suspend fun addToCart(userId: Long, boardGameId: Long, quantity: Int) {
        val existingItem = cartItemDao.getCartItem(userId, boardGameId)
        if (existingItem != null) {
            existingItem.quantity += quantity
            cartItemDao.updateCartItem(existingItem.copy(updatedAt = Date()))
        } else {
            cartItemDao.insertCartItem(CartItem(userId, boardGameId, quantity))
        }
    }

    suspend fun updateCartItemQuantity(userId: Long, boardGameId: Long, quantity: Int) {
        val cartItem = cartItemDao.getCartItem(userId, boardGameId)
        cartItem?.let {
            if (quantity <= 0) {
                cartItemDao.deleteCartItem(it)
            } else {
                cartItemDao.updateCartItem(it.copy(quantity = quantity, updatedAt = Date()))
            }
        }
    }

    suspend fun removeFromCart(userId: Long, boardGameId: Long) {
        val cartItem = cartItemDao.getCartItem(userId, boardGameId)
        cartItem?.let {
            cartItemDao.deleteCartItem(it)
        }
    }

    suspend fun clearCart(userId: Long) {
        cartItemDao.clearCart(userId)
    }
}