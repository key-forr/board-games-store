package com.example.boardGamesStore.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import kotlinx.coroutines.launch

class BoardGameViewModel(private val repository: BoardGameRepository) : ViewModel() {
    val allBoardGames: LiveData<List<BoardGame>> = repository.allBoardGames.map { games ->
        games.filter { it.isActive }
    }

    suspend fun getBoardGameById(id: Long): BoardGame? {
        return repository.getBoardGameById(id)
    }

    fun insertBoardGame(boardGame: BoardGame) = viewModelScope.launch {
        repository.insertBoardGame(boardGame)
    }

    fun updateBoardGame(boardGame: BoardGame) = viewModelScope.launch {
        repository.updateBoardGame(boardGame)
    }

    fun deleteBoardGame(id: Long) = viewModelScope.launch {
        repository.deleteBoardGame(id)
    }
}