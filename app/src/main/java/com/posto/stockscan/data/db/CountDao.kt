package com.posto.stockscan.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** Consultas à tabela "count_entries". */
@Dao
interface CountDao {
    @Query("SELECT * FROM count_entries WHERE productId = :productId LIMIT 1")
    suspend fun findByProduct(productId: Long): CountEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: CountEntry): Long

    @Query("DELETE FROM count_entries")
    suspend fun clear()
}
