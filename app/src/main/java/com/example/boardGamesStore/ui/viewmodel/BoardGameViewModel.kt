package com.example.boardGamesStore.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.repository.BoardGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardGameViewModel(private val repository: BoardGameRepository) : ViewModel() {
    val allBoardGames: LiveData<List<BoardGame>> = repository.allBoardGames.map { games ->
        games.filter { it.isActive }
    }


    suspend fun getBoardGameById(id: Long): BoardGame? {
        return repository.getBoardGameById(id)
    }

    fun searchBoardGames(query: String): LiveData<List<BoardGame>> {
        return repository.searchBoardGames(query)
    }

    fun filterBoardGamesByPrice(minPrice: Double, maxPrice: Double): LiveData<List<BoardGame>> {
        return repository.filterBoardGamesByPrice(minPrice, maxPrice)
    }

    fun getPriceRange(): Pair<Double, Double> {
        var priceRange = Pair(0.0, 0.0)
        viewModelScope.launch {
            priceRange = repository.getPriceRange()
        }
        return priceRange
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