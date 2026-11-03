package com.posto.stockscan.ui.screens.scanner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posto.stockscan.data.repository.ProductRepository
import kotlinx.coroutines.launch

/**
 * Prova de conceito do leitor (base da US3).
 * A câmera lê continuamente, mas depois do primeiro código o resultado fica "congelado"
 * até o usuário tocar em "Ler outro" — assim o mesmo produto não é lido várias vezes.
 */
class ScannerViewModel(private val products: ProductRepository) : ViewModel() {

    /** `null` = câmera à procura de um código. */
    var result by mutableStateOf<ScanResult?>(null)
        private set

    var torchOn by mutableStateOf(false)
        private set

    /** Quantos códigos foram lidos nesta sessão (só para o teste). */
    var readCount by mutableStateOf(0)
        private set

    var newProductName by mutableStateOf("")
        private set

    var message by mutableStateOf<String?>(null)
        private set

    /** Chamado pela câmera a cada código encontrado. Devolve `true` se o código foi aceito. */
    fun onBarcodeDetected(code: String, formatName: String): Boolean {
        if (result != null) return false // já há um resultado na tela: ignora
        result = ScanResult(code = code, formatName = formatName)
        readCount++
        lookup(code)
        return true
    }

    private fun lookup(code: String) {
        viewModelScope.launch {
            val product = products.findByBarcode(code)
            result = result?.copy(searching = false, product = product)
        }
    }

    fun scanAgain() {
        result = null
        newProductName = ""
        message = null
    }

    fun toggleTorch() { torchOn = !torchOn }

    fun onNewProductName(value: String) { newProductName = value; message = null }

    /** Cadastro rápido só para testar (o cadastro completo de produtos é o Sprint 2). */
    fun registerScannedProduct() {
        val r = result ?: return
        if (newProductName.isBlank()) {
            message = "Informe o nome do produto."
            return
        }
        viewModelScope.launch {
            val created = products.add(r.code, newProductName)
            if (created != null) {
                result = r.copy(product = created)
                newProductName = ""
                message = "Produto cadastrado. Leia o mesmo código de novo para ver que agora é reconhecido."
            } else {
                message = "Este código já está cadastrado."
            }
        }
    }
}
