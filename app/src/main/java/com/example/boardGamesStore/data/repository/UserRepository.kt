package com.example.boardGamesStore.data.repository

import com.example.boardGamesStore.data.dao.UserDao
import com.example.boardGamesStore.data.entity.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User): Boolean {
        val existingUser = userDao.getUserByEmail(user.email)
        return if (existingUser == null) {
            userDao.insertUser(user)
            true
        } else {
            false
        }
    }

    suspend fun insertUser(user: User){
        return userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}