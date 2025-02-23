package com.example.tutor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}