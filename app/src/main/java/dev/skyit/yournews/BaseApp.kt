package dev.skyit.yournews

import android.app.Application
import dev.skyit.yournews.dependencies.apiModule
import dev.skyit.yournews.dependencies.repositoryModule
import dev.skyit.yournews.dependencies.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())


        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApp)

            modules(listOf(
                apiModule,
                repositoryModule,
                viewModelsModule
            ))
        }
    }
}