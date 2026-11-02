package com.posto.stockscan

import android.app.Application
import com.posto.stockscan.data.db.AppDatabase
import com.posto.stockscan.data.repository.SessionManager
import com.posto.stockscan.data.repository.UserRepository

/** Injeção de dependências manual: um único contentor para toda a app. */
class AppContainer(app: Application) {
    val database: AppDatabase by lazy { AppDatabase.build(app) }
    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }
    val session = SessionManager()
}

class StockScanApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
