package com.example.tutor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tutor.data.entity.User
import com.example.tutor.data.dao.UserDao
import com.example.tutor.data.entity.Cart
import com.example.tutor.data.entity.Game
import com.example.tutor.data.entity.Order
import com.example.tutor.data.entity.OrderDetail


@Database(
    entities = [User::class, OrderDetail::class, Order::class, Game::class, Cart::class],
    version = 1
)
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