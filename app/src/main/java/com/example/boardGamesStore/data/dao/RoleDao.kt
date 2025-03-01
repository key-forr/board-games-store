package com.example.boardGamesStore.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.boardGamesStore.data.entity.Role

@Dao
interface RoleDao {
    @Insert
    suspend fun insertRole(role: Role)
}