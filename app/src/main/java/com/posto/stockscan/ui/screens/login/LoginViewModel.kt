package com.posto.stockscan.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posto.stockscan.data.repository.SessionManager
import com.posto.stockscan.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val users: UserRepository,
    private val session: SessionManager,
) : ViewModel() {
    var state by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(value: String) { state = state.copy(username = value, error = null) }
    fun onPasswordChange(value: String) { state = state.copy(password = value, error = null) }

    fun login() {
        if (state.loading) return
        if (state.username.isBlank() || state.password.isEmpty()) {
            state = state.copy(error = "Preencha o utilizador e a senha.")
            return
        }
        state = state.copy(loading = true, error = null)
        viewModelScope.launch {
            val user = users.authenticate(state.username, state.password)
            state = if (user != null) {
                session.login(user)
                state.copy(loading = false, password = "", loggedIn = true)
            } else {
                state.copy(loading = false, password = "", error = "Utilizador ou senha inválidos.")
            }
        }
    }
}
