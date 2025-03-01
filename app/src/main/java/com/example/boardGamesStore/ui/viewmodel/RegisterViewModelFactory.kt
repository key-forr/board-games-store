package com.example.boardGamesStore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.boardGamesStore.domain.RegisterUserUseCase

class RegisterViewModelFactory(private val registerUserUseCase: RegisterUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(registerUserUseCase) as T
    }
}