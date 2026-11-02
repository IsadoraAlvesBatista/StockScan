package com.posto.stockscan.ui.screens.setup

/** Tudo o que a tela de primeiro acesso mostra num dado momento. */
data class SetupUiState(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val confirmation: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false,
)
