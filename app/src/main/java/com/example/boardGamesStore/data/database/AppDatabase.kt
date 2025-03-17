// data/database/AppDatabase.kt
package com.example.boardGamesStore.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.boardGamesStore.data.dao.BoardGameDao
import com.example.boardGamesStore.data.dao.CartItemDao
import com.example.boardGamesStore.data.dao.UserDao
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.entity.CartItem
import com.example.boardGamesStore.data.entity.User
import com.example.boardGamesStore.data.util.DateConverter

@Database(
    entities = [User::class, BoardGame::class, CartItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun boardGameDao(): BoardGameDao
    abstract fun cartItemDao(): CartItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "board_games_store_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}