package com.posto.stockscan.ui.screens.login

/** Tudo o que a tela de login mostra num dado momento. */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false,
)
