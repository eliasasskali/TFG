package com.eliasasskali.tfg

import android.app.Application
import com.eliasasskali.tfg.android.core.di.appModule
import com.eliasasskali.tfg.android.core.di.dataModule
import com.eliasasskali.tfg.android.core.di.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(appModule, dataModule(this@App), remoteModule)
            )
        }
    }
}