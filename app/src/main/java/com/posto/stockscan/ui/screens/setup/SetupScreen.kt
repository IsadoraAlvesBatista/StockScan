package com.posto.stockscan.ui.screens.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.posto.stockscan.ui.FORM_MAX_WIDTH
import com.posto.stockscan.ui.TextInput

@Composable
fun SetupScreen(vm: SetupViewModel, onDone: () -> Unit) {
    val s = vm.state
    LaunchedEffect(s.done) { if (s.done) onDone() }

    Surface(Modifier.fillMaxSize()) {
        // Em tablet/horizontal o formulário fica centrado com largura máxima.
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .widthIn(max = FORM_MAX_WIDTH)
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Spacer(Modifier.height(24.dp))
                Text("Bem-vindo ao StockScan", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Text(
                    "Este é o primeiro acesso. Crie a conta de Administrador — com ela poderá depois criar as contas dos funcionários.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                val enabled = !s.loading
                TextInput(s.name, vm::onName, "Nome", Modifier.fillMaxWidth(), enabled = enabled)
                TextInput(s.username, vm::onUsername, "Usuário", Modifier.fillMaxWidth(), enabled = enabled)
                TextInput(s.password, vm::onPassword, "Senha", Modifier.fillMaxWidth(), password = true, enabled = enabled)
                TextInput(
                    s.confirmation, vm::onConfirmation, "Confirmar senha", Modifier.fillMaxWidth(),
                    password = true, imeAction = ImeAction.Done, enabled = enabled,
                )
                s.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Button(onClick = vm::submit, enabled = enabled, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    if (s.loading) CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                    else Text("Criar Administrador")
                }
            }
        }
    }
}
