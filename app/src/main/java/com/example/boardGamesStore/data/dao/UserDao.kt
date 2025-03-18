package com.example.boardGamesStore.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM Users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM Users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM Users WHERE id = :userId")
    suspend fun deleteUser(userId: Long)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): LiveData<List<User>>
}