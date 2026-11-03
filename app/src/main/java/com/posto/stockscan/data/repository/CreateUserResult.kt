package com.posto.stockscan.data.repository

import com.posto.stockscan.data.db.User

/** Resultado de [UserRepository.createUser]: ou deu certo (com o utilizador criado) ou deu erro (com a mensagem). */
sealed interface CreateUserResult {
    data class Success(val user: User) : CreateUserResult
    data class Error(val message: String) : CreateUserResult
}
