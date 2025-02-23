package com.example.tutor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Roles")
data class Role(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "role_name") val roleName: String,
)