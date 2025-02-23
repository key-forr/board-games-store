package com.example.tutor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tutor.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM User")
    suspend fun clearTable()
}