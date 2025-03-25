package com.example.boardGamesStore.data.repository

import androidx.lifecycle.LiveData
import com.example.boardGamesStore.data.dao.BoardGameDao
import com.example.boardGamesStore.data.entity.BoardGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoardGameRepository(private val boardGameDao: BoardGameDao) {
    val allBoardGames: LiveData<List<BoardGame>> = boardGameDao.getAllBoardGames()

    suspend fun getBoardGameById(id: Long): BoardGame? {
        return boardGameDao.getBoardGameById(id)
    }

    suspend fun insertBoardGame(boardGame: BoardGame): Long {
        return boardGameDao.insertBoardGame(boardGame)
    }

    fun searchBoardGames(query: String): LiveData<List<BoardGame>> {
        return boardGameDao.searchBoardGames("%$query%")
    }

    fun filterBoardGamesByPrice(minPrice: Double, maxPrice: Double): LiveData<List<BoardGame>> {
        return boardGameDao.filterBoardGamesByPrice(minPrice, maxPrice)
    }

    // Метод для отримання мінімальної та максимальної цін
    suspend fun getPriceRange(): Pair<Double, Double> = withContext(Dispatchers.IO) {
        val minPrice = boardGameDao.getMinPrice() ?: 0.0
        val maxPrice = boardGameDao.getMaxPrice() ?: 0.0
        Pair(minPrice, maxPrice)
    }

    suspend fun updateBoardGame(boardGame: BoardGame) {
        boardGameDao.updateBoardGame(boardGame)
    }

    suspend fun deactivateBoardGame(id: Long) {
        boardGameDao.deactivateBoardGame(id)
    }

    suspend fun deleteBoardGame(id: Long) {
        boardGameDao.deleteBoardGame(id)
    }
}