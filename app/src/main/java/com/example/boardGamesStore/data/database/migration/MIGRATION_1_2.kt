package com.example.boardGamesStore.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE board_games ADD COLUMN is_active INTEGER NOT NULL DEFAULT 1")
    }
}