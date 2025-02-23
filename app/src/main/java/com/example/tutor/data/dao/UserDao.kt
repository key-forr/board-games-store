package com.example.tutor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tutor.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM Users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}