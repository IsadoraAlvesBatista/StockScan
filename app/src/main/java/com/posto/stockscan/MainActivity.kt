package com.posto.stockscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.posto.stockscan.ui.navigation.StockScanNavHost
import com.posto.stockscan.ui.theme.StockScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as StockScanApp).container
        setContent {
            StockScanTheme {
                StockScanNavHost(container)
            }
        }
    }
}
