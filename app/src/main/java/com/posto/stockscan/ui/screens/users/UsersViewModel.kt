package com.posto.stockscan.ui.screens.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.posto.stockscan.data.db.Role
import com.posto.stockscan.data.db.User
import com.posto.stockscan.data.repository.CreateUserResult
import com.posto.stockscan.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NewUserForm(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val confirmation: String = "",
    val role: Role = Role.EMPLOYEE,
    val saving: Boolean = false,
    val error: String? = null,
)

class UsersViewModel(private val repo: UserRepository) : ViewModel() {

    val users: StateFlow<List<User>> =
        repo.observeUsers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** `null` = formulário fechado. */
    var form by mutableStateOf<NewUserForm?>(null)
        private set

    var message by mutableStateOf<String?>(null)
        private set

    fun openForm() { form = NewUserForm() }
    fun closeForm() { if (form?.saving != true) form = null }
    fun messageShown() { message = null }

    fun update(transform: NewUserForm.() -> NewUserForm) {
        form = form?.transform()?.copy(error = null)
    }

    fun save() {
        val f = form ?: return
        if (f.saving) return
        form = f.copy(saving = true, error = null)
        viewModelScope.launch {
            when (val r = repo.createUser(f.name, f.username, f.password, f.confirmation, f.role)) {
                is CreateUserResult.Success -> {
                    form = null
                    message = "Usuário \"${r.user.username}\" criado."
                }
                is CreateUserResult.Error -> form = f.copy(saving = false, error = r.message)
            }
        }
    }
}
