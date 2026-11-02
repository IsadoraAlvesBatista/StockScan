package com.posto.stockscan.ui.screens.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posto.stockscan.data.db.Role
import com.posto.stockscan.data.repository.CreateUserResult
import com.posto.stockscan.data.repository.SessionManager
import com.posto.stockscan.data.repository.UserRepository
import kotlinx.coroutines.launch

data class SetupUiState(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val confirmation: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false,
)

/** Primeiro arranque: cria a conta de Administrador (evita senhas padrão de fábrica). */
class SetupViewModel(
    private val users: UserRepository,
    private val session: SessionManager,
) : ViewModel() {
    var state by mutableStateOf(SetupUiState())
        private set

    fun onName(v: String) { state = state.copy(name = v, error = null) }
    fun onUsername(v: String) { state = state.copy(username = v, error = null) }
    fun onPassword(v: String) { state = state.copy(password = v, error = null) }
    fun onConfirmation(v: String) { state = state.copy(confirmation = v, error = null) }

    fun submit() {
        if (state.loading) return
        state = state.copy(loading = true, error = null)
        viewModelScope.launch {
            if (users.hasAnyUser()) {
                state = state.copy(loading = false, error = "A configuração inicial já foi feita. Reinicie a aplicação.")
                return@launch
            }
            val s = state
            state = when (val r = users.createUser(s.name, s.username, s.password, s.confirmation, Role.ADMIN)) {
                is CreateUserResult.Success -> {
                    session.login(r.user)
                    s.copy(loading = false, password = "", confirmation = "", done = true)
                }
                is CreateUserResult.Error -> s.copy(loading = false, error = r.message)
            }
        }
    }
}
