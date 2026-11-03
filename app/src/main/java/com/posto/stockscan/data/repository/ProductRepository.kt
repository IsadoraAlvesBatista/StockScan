package com.posto.stockscan.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.posto.stockscan.data.db.Product
import com.posto.stockscan.data.db.ProductDao

/** Acesso aos produtos. Nesta fase (prova de conceito) só tem o necessário para o leitor. */
class ProductRepository(private val dao: ProductDao) {

    suspend fun findByBarcode(barcode: String): Product? = dao.findByBarcode(barcode.trim())

    /** Regista um produto. Devolve o produto criado, ou `null` se o código já existir. */
    suspend fun add(barcode: String, name: String): Product? {
        val product = Product(barcode = barcode.trim(), name = name.trim())
        return try {
            product.copy(id = dao.insert(product))
        } catch (e: SQLiteConstraintException) {
            null
        }
    }
}
