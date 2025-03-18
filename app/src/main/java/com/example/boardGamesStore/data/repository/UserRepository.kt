package com.example.boardGamesStore.data.repository

import androidx.lifecycle.LiveData
import com.example.boardGamesStore.data.dao.UserDao
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.entity.User

class UserRepository(private val userDao: UserDao) {

    // Add the missing method to get all users
    fun allUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun registerUser(user: User): Boolean {
        val existingUser = userDao.getUserByEmail(user.email)
        return if (existingUser == null) {
            userDao.insertUser(user)
            true
        } else {
            false
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(userId: Long) {
        userDao.deleteUser(userId)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
}