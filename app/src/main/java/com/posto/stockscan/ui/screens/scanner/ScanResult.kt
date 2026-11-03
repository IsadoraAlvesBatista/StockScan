package com.posto.stockscan.ui.screens.scanner

import com.posto.stockscan.data.db.Product

/** O que foi lido pela câmera e o resultado da procura na base de dados. */
data class ScanResult(
    val code: String,
    val formatName: String,
    /** `true` enquanto procura o código na base de dados. */
    val searching: Boolean = true,
    /** Produto encontrado, ou `null` se o código não estiver cadastrado. */
    val product: Product? = null,
)
