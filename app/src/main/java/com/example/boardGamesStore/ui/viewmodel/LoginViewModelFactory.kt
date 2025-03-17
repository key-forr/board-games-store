package com.example.boardGamesStore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.boardGamesStore.data.repository.UserRepository
import com.example.boardGamesStore.domain.LoginUserUseCase

class LoginViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val loginUserUseCase = LoginUserUseCase(userRepository)
        return LoginViewModel(loginUserUseCase, userRepository) as T
    }
}
