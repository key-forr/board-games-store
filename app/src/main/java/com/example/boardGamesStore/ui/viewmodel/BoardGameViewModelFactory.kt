package com.example.boardGamesStore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.boardGamesStore.data.repository.BoardGameRepository

class BoardGameViewModelFactory(private val repository: BoardGameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardGameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}