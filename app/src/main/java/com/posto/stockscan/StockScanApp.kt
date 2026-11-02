package com.posto.stockscan

import android.app.Application

/** Classe da aplicação: criada antes de qualquer tela. Registada no AndroidManifest.xml. */
class StockScanApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
