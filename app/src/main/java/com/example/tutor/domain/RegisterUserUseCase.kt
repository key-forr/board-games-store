package com.example.tutor.domain

import com.example.tutor.data.entity.User
import com.example.tutor.data.repository.UserRepository

class RegisterUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(username: String, email: String, password: String): Boolean {
        if (email.isEmpty() || password.length < 6) return false
        val hashedPassword = hashPassword(password)
        val user = User(username = username, email = email, password = hashedPassword, roleId = 1)
        return userRepository.registerUser(user)
    }

    private fun hashPassword(password: String): String {
        return password.hashCode().toString()
    }
}
