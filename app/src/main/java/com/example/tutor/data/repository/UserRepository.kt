package com.example.tutor.data.repository

import com.example.tutor.data.dao.UserDao
import com.example.tutor.data.entity.User

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

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}