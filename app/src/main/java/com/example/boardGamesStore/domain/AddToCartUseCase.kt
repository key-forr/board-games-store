package com.example.boardGamesStore.domain

import com.example.boardGamesStore.data.repository.CartRepository

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(userId: Long, boardGameId: Long, quantity: Int) {
        cartRepository.addToCart(userId, boardGameId, quantity)
    }
}