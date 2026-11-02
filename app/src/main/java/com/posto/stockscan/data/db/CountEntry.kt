package com.posto.stockscan.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabela "count_entries": linha da contagem em curso (US4). Existe no máximo
 * uma linha por produto; a tabela é esvaziada quando o relatório é gerado (US5).
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
