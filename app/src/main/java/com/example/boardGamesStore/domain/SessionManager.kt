package com.example.boardGamesStore.domain

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "BoardGamesStorePrefs"
        private const val USER_ID = "user_id"
        private const val USER_EMAIL = "user_email"
        private const val USER_ROLE = "user_role"
        private const val IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUserSession(userId: Long, email: String, role: Long) {
        editor.putLong(USER_ID, userId)
        editor.putString(USER_EMAIL, email)
        editor.putLong(USER_ROLE, role)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(USER_ID, -1L)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(USER_EMAIL, null)
    }

    fun getUserRole(): Long {
        return sharedPreferences.getLong(USER_ROLE, 1L)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}