package com.example.tutor.data.repository

import com.example.tutor.data.dao.UserDao
import com.example.tutor.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    suspend fun clearTable() {
        userDao.clearTable()
    }
}