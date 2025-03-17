// domain/LoginUserUseCase.kt (якщо ще не створений)
package com.example.boardGamesStore.domain

import com.example.boardGamesStore.data.entity.User
import com.example.boardGamesStore.data.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String): String {
        val user = userRepository.getUserByEmail(email) ?: return "error"
        return if (user.password == password) {
            when (user.roleId) {
                1L -> "client"
                2L -> "admin"
                else -> "error"
            }
        } else {
            "error"
        }
    }
}