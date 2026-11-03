package com.posto.stockscan.domain

import java.util.Locale

/** Regras de validação dos dados de utilizador. Sem dependências Android para ser testável. */
object UserValidator {
    const val MIN_USERNAME = 3
    const val MIN_PASSWORD = 6
    private val USERNAME_REGEX = Regex("^[a-z0-9._-]+$")

    fun normalizeUsername(raw: String): String = raw.trim().lowercase(Locale.ROOT)

    /** Devolve a mensagem de erro ou `null` se os dados forem válidos. */
    fun validate(name: String, username: String, password: String, confirmation: String): String? {
        val user = normalizeUsername(username)
        return when {
            name.isBlank() -> "Indique o nome."
            user.length < MIN_USERNAME -> "O nome de utilizador deve ter pelo menos $MIN_USERNAME caracteres."
            !USERNAME_REGEX.matches(user) -> "O nome de utilizador só pode ter letras, números, ponto, hífen ou sublinhado (sem espaços nem acentos)."
            password.length < MIN_PASSWORD -> "A senha deve ter pelo menos $MIN_PASSWORD caracteres."
            password != confirmation -> "As senhas não coincidem."
            else -> null
        }
    }

    /**
     * Regras para desativar uma conta (US01). Devolve a mensagem de erro ou `null` se for permitido.
     * @param activeAdmins número de Administradores ativos neste momento.
     */
    fun deactivationError(targetId: Long, targetIsAdmin: Boolean, currentUserId: Long, activeAdmins: Int): String? = when {
        targetId == currentUserId -> "Não pode desativar a sua própria conta."
        targetIsAdmin && activeAdmins <= 1 -> "Tem de existir pelo menos um Administrador ativo."
        else -> null
    }
}
