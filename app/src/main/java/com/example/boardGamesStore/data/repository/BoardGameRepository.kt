// data/repository/BoardGameRepository.kt
package com.example.boardGamesStore.data.repository

import androidx.lifecycle.LiveData
import com.example.boardGamesStore.data.dao.BoardGameDao
import com.example.boardGamesStore.data.entity.BoardGame

class BoardGameRepository(private val boardGameDao: BoardGameDao) {
    val allBoardGames: LiveData<List<BoardGame>> = boardGameDao.getAllBoardGames()

    suspend fun getBoardGameById(id: Long): BoardGame? {
        return boardGameDao.getBoardGameById(id)
    }

    suspend fun insertBoardGame(boardGame: BoardGame): Long {
        return boardGameDao.insertBoardGame(boardGame)
    }

    suspend fun updateBoardGame(boardGame: BoardGame) {
        boardGameDao.updateBoardGame(boardGame)
    }

    suspend fun deleteBoardGame(id: Long) {
        boardGameDao.deleteBoardGame(id)
    }
}