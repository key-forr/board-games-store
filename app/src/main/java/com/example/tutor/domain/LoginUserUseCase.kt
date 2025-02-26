package com.example.tutor.domain

import com.example.tutor.data.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String): String {
        val user = userRepository.getUserByEmail(email)
        return if (user != null && user.password == password.hashCode().toString()) {
            if (user.roleId == 1L) "client" else "admin"
        } else {
            "error"
        }
    }
}
