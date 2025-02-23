package com.example.tutor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class DbHelper : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: DbHelper? = null

        fun getDb(context: Context): DbHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbHelper::class.java,
                    "board-games-store.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}