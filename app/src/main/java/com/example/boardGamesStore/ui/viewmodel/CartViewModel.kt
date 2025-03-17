// ui/viewmodel/CartViewModel.kt
package com.example.boardGamesStore.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardGamesStore.data.entity.CartWithBoardGames
import com.example.boardGamesStore.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private var userId: Long = -1

    fun setUserId(id: Long) {
        userId = id
    }

    fun getCartWithBoardGames(): LiveData<List<CartWithBoardGames>> {
        return repository.getCartWithBoardGames(userId)
    }

    fun getCartItemsCount(): LiveData<Int> {
        return repository.getCartItemsCount(userId)
    }

    fun addToCart(boardGameId: Long, quantity: Int) = viewModelScope.launch {
        repository.addToCart(userId, boardGameId, quantity)
    }

    fun updateCartItemQuantity(boardGameId: Long, quantity: Int) = viewModelScope.launch {
        repository.updateCartItemQuantity(userId, boardGameId, quantity)
    }

    fun removeFromCart(boardGameId: Long) = viewModelScope.launch {
        repository.removeFromCart(userId, boardGameId)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart(userId)
    }
}