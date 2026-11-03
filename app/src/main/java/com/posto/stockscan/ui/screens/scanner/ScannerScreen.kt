package com.posto.stockscan.ui.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.posto.stockscan.ui.FORM_MAX_WIDTH
import com.posto.stockscan.ui.TextInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(vm: ScannerViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasPermission = granted
    }
    LaunchedEffect(Unit) { if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA) }

    // "Bip" ao ler um código.
    val beeper = remember { runCatching { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80) }.getOrNull() }
    DisposableEffect(Unit) { onDispose { beeper?.release() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teste do leitor") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar") }
                },
                actions = {
                    if (hasPermission) {
                        TextButton(onClick = vm::toggleTorch) { Text(if (vm.torchOn) "Lanterna: ligada" else "Lanterna: desligada") }
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            if (!hasPermission) {
                PermissionMissing(onRequest = { permissionLauncher.launch(Manifest.permission.CAMERA) })
                return@Box
            }

            CameraBarcodePreview(
                torchOn = vm.torchOn,
                onBarcode = { code, format ->
                    if (vm.onBarcodeDetected(code, format)) beeper?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                },
                modifier = Modifier.fillMaxSize(),
            )

            // Moldura de ajuda para apontar o código.
            if (vm.result == null) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.8f)
                        .height(140.dp)
                        .border(BorderStroke(3.dp, Color.White), RoundedCornerShape(12.dp)),
                )
                Text(
                    "Aponte para o código de barras",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            ResultPanel(vm, Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun ResultPanel(vm: ScannerViewModel, modifier: Modifier) {
    val r = vm.result
    Card(modifier.padding(12.dp).widthIn(max = FORM_MAX_WIDTH).fillMaxWidth()) {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (r == null) {
                Text("À procura de código… (lidos nesta sessão: ${vm.readCount})", style = MaterialTheme.typography.bodyMedium)
                return@Column
            }
            Text("Código lido (${r.formatName})", style = MaterialTheme.typography.labelLarge)
            Text(r.code, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            when {
                r.searching -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Text("A procurar na base de dados…")
                }
                r.product != null -> Text(
                    "✅ Produto encontrado: ${r.product.name}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                else -> {
                    Text("⚠️ Código não registado", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleMedium)
                    TextInput(
                        vm.newProductName, vm::onNewProductName, "Nome do produto (registo de teste)",
                        Modifier.fillMaxWidth(), imeAction = ImeAction.Done,
                    )
                    OutlinedButton(onClick = vm::registerScannedProduct, modifier = Modifier.fillMaxWidth()) {
                        Text("Registar este código")
                    }
                }
            }
            vm.message?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Button(onClick = vm::scanAgain, modifier = Modifier.fillMaxWidth()) { Text("Ler outro") }
        }
    }
}

@Composable
private fun PermissionMissing(onRequest: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Para ler códigos de barras, a aplicação precisa de acesso à câmara.", style = MaterialTheme.typography.titleMedium)
        Button(onClick = onRequest) { Text("Permitir câmara") }
        Text(
            "Se recusou com «Não perguntar novamente», ative em Definições → Aplicações → StockScan → Autorizações → Câmara.",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
