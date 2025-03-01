package com.example.tutor.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutor.data.entity.User
import com.example.tutor.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Pair<Boolean, Long?>>()
    val loginResult: LiveData<Pair<Boolean, Long?>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.password == password.hashCode().toString()) {
                _loginResult.postValue(Pair(true, user.roleId))
            } else {
                _loginResult.value = Pair(false, null)
            }
        }
    }

    fun loginWithGoogle(email: String, googleId: String, photoUrl: String?) {
        viewModelScope.launch {
            var user = userRepository.getUserByEmail(email)

            if (user == null) {
                user = User(email = email, googleId = googleId, photoUrl = photoUrl)
                userRepository.insertUser(user)
                Log.d("Database", "Збережено нового користувача: $user")
            } else {
                Log.d("Database", "Користувач уже існує: $user")
            }

            _loginResult.postValue(Pair(true, user.roleId))
        }
    }
}
