package com.posto.stockscan.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Tabela "users": as contas de acesso ao app (US1). */
@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)],
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    /** Sempre guardado em minúsculas: o login não diferencia maiúsculas. */
    val username: String,
    /** Hash PBKDF2 da senha — a senha em texto nunca é guardada. */
    val passwordHash: String,
    val role: Role,
    val active: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
)
