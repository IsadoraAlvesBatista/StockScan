package com.posto.stockscan.data.db

/**
 * Perfis de acesso (US1).
 * Na base de dados é guardado o nome da constante ("ADMIN" / "EMPLOYEE");
 * [label] é só o texto mostrado nas telas.
 */
enum class Role(val label: String) {
    ADMIN("Administrador"),
    EMPLOYEE("Funcionário"),
}
