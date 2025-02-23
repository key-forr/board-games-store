package com.example.tutor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutor.domain.RegisterUserUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> get() = _registrationResult

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = registerUserUseCase.execute(username, email, password)
            _registrationResult.value = result
        }
    }
}
