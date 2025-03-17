package com.example.boardGamesStore.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardGamesStore.data.entity.User
import com.example.boardGamesStore.data.repository.UserRepository
import com.example.boardGamesStore.domain.LoginUserUseCase
import kotlinx.coroutines.launch

// Нова структура даних для результату входу
data class LoginResult(val success: Boolean, val userId: Long, val email: String, val roleId: Long)

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val role = loginUserUseCase.execute(email, password)
            when (role) {
                "client" -> {
                    val user = userRepository.getUserByEmail(email)
                    if (user != null) {
                        _loginResult.postValue(LoginResult(true, user.id ?: -1L, user.email, user.roleId ?: 1L))
                    } else {
                        _loginResult.postValue(LoginResult(false, -1L, "", -1L))
                    }
                }
                "admin" -> {
                    val user = userRepository.getUserByEmail(email)
                    if (user != null) {
                        _loginResult.postValue(LoginResult(true, user.id ?: -2L, user.email, user.roleId ?: 2L))
                    } else {
                        _loginResult.postValue(LoginResult(false, -1L, "", -1L))
                    }
                }
                else -> _loginResult.postValue(LoginResult(false, -1L, "", -1L))
            }
        }
    }

    fun loginWithGoogle(email: String, googleId: String, photoUrl: String?) {
        viewModelScope.launch {
            var user = userRepository.getUserByEmail(email)

            if (user == null) {
                user = User(email = email, googleId = googleId, photoUrl = photoUrl)
                val userId = userRepository.insertUser(user)
                Log.d("Database", "Збережено нового користувача: $user")

                // Припускаємо, що новий користувач отримує роль "client" (1L)
                _loginResult.postValue(LoginResult(true, userId, email, 1L))
            } else {
                Log.d("Database", "Користувач уже існує: $user")
                _loginResult.postValue(LoginResult(true, user.id ?: -1L, user.email, user.roleId ?: 1L))
            }
        }
    }
}