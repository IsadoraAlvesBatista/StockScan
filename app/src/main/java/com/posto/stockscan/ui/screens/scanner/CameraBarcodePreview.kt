package com.posto.stockscan.ui.screens.scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning

/**
 * Mostra a imagem da câmara traseira e chama [onBarcode] cada vez que o ML Kit
 * encontra um código de barras na imagem.
 *
 * Como funciona:
 *  - CameraX (LifecycleCameraController) liga a câmara e mostra o vídeo num PreviewView;
 *  - cada imagem (≈30 por segundo) passa pelo MlKitAnalyzer, que usa o leitor do ML Kit;
 *  - o ML Kit corre no próprio celular, sem internet.
 */
@Composable
fun CameraBarcodePreview(
    torchOn: Boolean,
    onBarcode: (code: String, formatName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnBarcode by rememberUpdatedState(onBarcode)

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS) // a pré-visualização está sempre ativa
        }
    }

    DisposableEffect(lifecycleOwner) {
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(BarcodeFormats.SUPPORTED[0], *BarcodeFormats.SUPPORTED.drop(1).toIntArray())
                .build(),
        )
        val executor = ContextCompat.getMainExecutor(context)
        controller.setImageAnalysisAnalyzer(
            executor,
            MlKitAnalyzer(listOf(scanner), ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL, executor) { result ->
                val barcode = result.getValue(scanner)?.firstOrNull { !it.rawValue.isNullOrBlank() }
                if (barcode != null) {
                    currentOnBarcode(barcode.rawValue!!, BarcodeFormats.name(barcode.format))
                }
            },
        )
        controller.bindToLifecycle(lifecycleOwner)

        onDispose {
            controller.clearImageAnalysisAnalyzer()
            controller.unbind()
            scanner.close()
        }
    }

    LaunchedEffect(torchOn) { controller.enableTorch(torchOn) }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                this.controller = controller
            }
        },
        modifier = modifier,
    )
}
