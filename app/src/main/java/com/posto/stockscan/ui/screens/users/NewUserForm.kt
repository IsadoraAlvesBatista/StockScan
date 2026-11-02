package com.posto.stockscan.ui.screens.users

import com.posto.stockscan.data.db.Role

/** Dados do formulário "Novo usuário". */
data class NewUserForm(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val confirmation: String = "",
    val role: Role = Role.EMPLOYEE,
    val saving: Boolean = false,
    val error: String? = null,
)
