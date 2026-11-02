package com.posto.stockscan.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Tabela "products": os produtos do cliente (US2). */
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
