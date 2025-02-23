package com.example.tutor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.tutor.data.entity.Role

@Dao
interface RoleDao {
    @Insert
    suspend fun insertRole(role: Role)
}