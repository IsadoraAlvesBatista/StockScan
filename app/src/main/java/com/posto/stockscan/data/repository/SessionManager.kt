package com.posto.stockscan.data.repository

import com.posto.stockscan.data.db.Role
import com.posto.stockscan.data.db.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Utilizador com sessão iniciada. Fica só em memória: ao fechar a app, é preciso entrar de novo. */
class SessionManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val isAdmin: Boolean get() = _currentUser.value?.role == Role.ADMIN

    fun login(user: User) { _currentUser.value = user }
    fun logout() { _currentUser.value = null }
}
