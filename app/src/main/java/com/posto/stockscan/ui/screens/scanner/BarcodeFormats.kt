package com.posto.stockscan.ui.screens.scanner

import com.google.mlkit.vision.barcode.common.Barcode

/** Formatos de código de barras que o leitor procura e o nome de cada um para mostrar na tela. */
object BarcodeFormats {
    /** EAN-13 é o padrão dos produtos no Brasil (começa com 789/790). */
    val SUPPORTED = intArrayOf(
        Barcode.FORMAT_EAN_13,
        Barcode.FORMAT_EAN_8,
        Barcode.FORMAT_UPC_A,
        Barcode.FORMAT_UPC_E,
        Barcode.FORMAT_CODE_128,
        Barcode.FORMAT_CODE_39,
        Barcode.FORMAT_ITF,
        Barcode.FORMAT_QR_CODE,
    )

    fun name(format: Int): String = when (format) {
        Barcode.FORMAT_EAN_13 -> "EAN-13"
        Barcode.FORMAT_EAN_8 -> "EAN-8"
        Barcode.FORMAT_UPC_A -> "UPC-A"
        Barcode.FORMAT_UPC_E -> "UPC-E"
        Barcode.FORMAT_CODE_128 -> "Code 128"
        Barcode.FORMAT_CODE_39 -> "Code 39"
        Barcode.FORMAT_ITF -> "ITF"
        Barcode.FORMAT_QR_CODE -> "QR Code"
        else -> "Outro"
    }
}
