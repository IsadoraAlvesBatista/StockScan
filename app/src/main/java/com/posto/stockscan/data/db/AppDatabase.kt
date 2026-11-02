package com.posto.stockscan.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de dados local (SQLite via Room). Os dados ficam no dispositivo e
 * sobrevivem ao fecho e reinício da aplicação.
 *
 * Ao alterar o esquema: incrementar [version] e adicionar uma Migration —
 * nunca usar migração destrutiva, para não perder dados do cliente.
 */
@Database(
    entities = [User::class, Product::class, CountEntry::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun countDao(): CountDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "stockscan.db")
                .build()
    }
}
