package com.posto.stockscan.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.posto.stockscan.ui.MENU_CARD_MIN_WIDTH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    session: SessionManager,
    onOpenUsers: () -> Unit,
    onOpenScanner: () -> Unit,
    onLogout: () -> Unit,
) {
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
        val u = user ?: return@Scaffold
        // Grade adaptável: no celular em pé fica 1 cartão por linha;
        // na horizontal ou em tablet cabem 2, 3 ou mais, conforme a largura.
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = MENU_CARD_MIN_WIDTH),
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Olá, ${u.name}", style = MaterialTheme.typography.headlineSmall)
                    AssistChip(onClick = {}, label = { Text(u.role.label) })
                }
            }
            item { MenuCard("Teste do leitor", "Protótipo da leitura de códigos (Sprint 2)", onClick = onOpenScanner) }
            item { MenuCard("Contagem de estoque", "Ler códigos de barras e registar quantidades", enabledHint = "Sprint 3 (01/12 – 15/12)") }
            item { MenuCard("Produtos", "Consultar e registar produtos", enabledHint = "Em desenvolvimento — Sprint 1 (até 16/11)") }
            item { MenuCard("Relatório", "Gerar o relatório da contagem", enabledHint = "Sprint 3 (01/12 – 15/12)") }
            if (u.role == Role.ADMIN) {
                item { MenuCard("Utilizadores", "Criar, consultar e desativar contas de acesso", onClick = onOpenUsers) }
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
    val modifier = Modifier.fillMaxWidth()
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier) { content() }
    } else {
        Card(modifier = modifier) { content() }
    }
}
