package com.example.boardGamesStore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.boardGamesStore.data.entity.BoardGame

@Dao
interface BoardGameDao {
    @Query("SELECT * FROM board_games ORDER BY is_active DESC, name ASC")
    fun getAllBoardGames(): LiveData<List<BoardGame>>

    @Query("SELECT * FROM board_games WHERE id = :id")
    suspend fun getBoardGameById(id: Long): BoardGame?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoardGame(boardGame: BoardGame): Long

    @Update
    suspend fun updateBoardGame(boardGame: BoardGame)

    @Query("UPDATE board_games SET is_active = 0 WHERE id = :id")
    suspend fun deactivateBoardGame(id: Long)

    @Query("DELETE FROM board_games WHERE id = :id")
    suspend fun deleteBoardGame(id: Long)
}