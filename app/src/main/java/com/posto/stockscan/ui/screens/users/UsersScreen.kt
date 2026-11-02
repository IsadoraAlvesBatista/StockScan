package com.posto.stockscan.ui.screens.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role as SemanticsRole
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posto.stockscan.data.db.Role
import com.posto.stockscan.ui.TextInput
import com.posto.stockscan.ui.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(vm: UsersViewModel, onBack: () -> Unit) {
    val users by vm.users.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(vm.message) {
        vm.message?.let {
            snackbar.showSnackbar(it)
            vm.messageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuários") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar") }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = vm::openForm,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Novo usuário") },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize()) {
            items(users, key = { it.id }) { user ->
                ListItem(
                    headlineContent = { Text(user.name) },
                    supportingContent = { Text("@${user.username}" + if (!user.active) " · inativo" else "") },
                    trailingContent = { Text(user.role.label, style = MaterialTheme.typography.labelMedium) },
                )
                HorizontalDivider()
            }
        }
    }

    vm.form?.let { NewUserDialog(it, vm) }
}

@Composable
private fun NewUserDialog(f: NewUserForm, vm: UsersViewModel) {
    val enabled = !f.saving
    AlertDialog(
        onDismissRequest = vm::closeForm,
        title = { Text("Novo usuário") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextInput(f.name, { v -> vm.update { copy(name = v) } }, "Nome", Modifier.fillMaxWidth(), enabled = enabled)
                TextInput(f.username, { v -> vm.update { copy(username = v) } }, "Usuário", Modifier.fillMaxWidth(), enabled = enabled)
                TextInput(f.password, { v -> vm.update { copy(password = v) } }, "Senha", Modifier.fillMaxWidth(), password = true, enabled = enabled)
                TextInput(
                    f.confirmation, { v -> vm.update { copy(confirmation = v) } }, "Confirmar senha", Modifier.fillMaxWidth(),
                    password = true, imeAction = ImeAction.Done, enabled = enabled,
                )
                Text("Perfil", style = MaterialTheme.typography.labelLarge)
                Role.entries.forEach { role ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = f.role == role,
                                enabled = enabled,
                                role = SemanticsRole.RadioButton,
                                onClick = { vm.update { copy(role = role) } },
                            ),
                    ) {
                        RadioButton(selected = f.role == role, onClick = null, enabled = enabled)
                        Text(role.label, Modifier.padding(start = 8.dp))
                    }
                }
                f.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = { TextButton(onClick = vm::save, enabled = enabled) { Text(if (f.saving) "Salvando…" else "Criar") } },
        dismissButton = { TextButton(onClick = vm::closeForm, enabled = enabled) { Text("Cancelar") } },
    )
}
