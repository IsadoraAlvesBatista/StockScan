package com.posto.stockscan.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Perfis de acesso (US1). */
enum class Role { ADMIN, EMPLOYEE }

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

/** Produto do cliente (US2). */
@Entity(
    tableName = "products",
    indices = [Index(value = ["barcode"], unique = true)],
)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val name: String,
    val category: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)

/**
 * Linha da contagem em curso (US4). Existe no máximo uma linha por produto;
 * a tabela é esvaziada quando o relatório é gerado (US5).
 */
@Entity(
    tableName = "count_entries",
    foreignKeys = [
        ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.SET_NULL),
    ],
    indices = [Index(value = ["productId"], unique = true), Index(value = ["userId"])],
)
data class CountEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val quantity: Double,
    val userId: Long?,
    val updatedAt: Long = System.currentTimeMillis(),
)
