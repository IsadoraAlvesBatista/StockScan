package com.posto.stockscan.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.posto.stockscan.data.db.Role
import com.posto.stockscan.data.repository.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(session: SessionManager, onOpenUsers: () -> Unit, onLogout: () -> Unit) {
    val user by session.currentUser.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("StockScan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val u = user ?: return@Column
            Text("Olá, ${u.name}", style = MaterialTheme.typography.headlineSmall)
            AssistChip(onClick = {}, label = { Text(u.role.label) })

            MenuCard("Contagem de estoque", "Ler códigos de barras e registrar quantidades", enabledHint = "Disponível no Sprint 3")
            MenuCard("Produtos", "Consultar e registrar produtos", enabledHint = "Disponível no Sprint 2")
            MenuCard("Relatório", "Gerar o relatório da contagem", enabledHint = "Disponível no Sprint 4")
            if (u.role == Role.ADMIN) {
                MenuCard("Usuários", "Criar e consultar contas de acesso", onClick = onOpenUsers)
            }
        }
    }
}

@Composable
private fun MenuCard(title: String, subtitle: String, onClick: (() -> Unit)? = null, enabledHint: String? = null) {
    val content: @Composable () -> Unit = {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            enabledHint?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline) }
        }
    }
    if (onClick != null) {
        Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) { content() }
    } else {
        Card(modifier = Modifier.fillMaxWidth()) { content() }
    }
}
