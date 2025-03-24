package com.example.boardGamesStore.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.boardGamesStore.data.dao.BoardGameDao
import com.example.boardGamesStore.data.dao.CartItemDao
import com.example.boardGamesStore.data.dao.OrderDao
import com.example.boardGamesStore.data.dao.UserDao
import com.example.boardGamesStore.data.database.migration.MIGRATION_2_3
import com.example.boardGamesStore.data.entity.BoardGame
import com.example.boardGamesStore.data.entity.CartItem
import com.example.boardGamesStore.data.entity.Order
import com.example.boardGamesStore.data.entity.OrderItem
import com.example.boardGamesStore.data.entity.User
import com.example.boardGamesStore.data.util.DateConverter

@Database(
    entities = [
        User::class,
        BoardGame::class,
        CartItem::class,
        Order::class,
        OrderItem::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun boardGameDao(): BoardGameDao
    abstract fun cartDao(): CartItemDao
    abstract fun orderDao(): OrderDao

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
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
