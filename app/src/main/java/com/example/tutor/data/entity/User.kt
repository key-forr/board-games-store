package com.example.tutor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val username: String? = null,
    val email: String,
    val password: String? = null,

    @ColumnInfo(name = "google_id")
    val googleId: String? = null,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null,

    @ColumnInfo(name = "auth_token")
    val authToken: String? = null,

    @ColumnInfo(name = "role_id")
    val roleId: Long = 1,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date? = null
)
