package com.example.tutor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tutor.data.dao.RoleDao
import com.example.tutor.data.entity.User
import com.example.tutor.data.dao.UserDao
import com.example.tutor.data.entity.Cart
import com.example.tutor.data.entity.CartItem
import com.example.tutor.data.entity.Game
import com.example.tutor.data.entity.Order
import com.example.tutor.data.entity.OrderItem
import com.example.tutor.data.entity.OrderStatus
import com.example.tutor.data.entity.Role


@Database(
    entities = [User::class, Order::class, Game::class, Cart::class,
        CartItem::class, OrderItem::class, OrderStatus::class, Role::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DbHelper : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getRoleDao(): RoleDao

    companion object {
        @Volatile
        private var INSTANCE: DbHelper? = null

        fun getDb(context: Context): DbHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbHelper::class.java,
                    "board-games-store.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}