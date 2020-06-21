package dev.skyit.yournews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.skyit.yournews.dependencies.*
import kotlinx.coroutines.InternalCoroutinesApi

import timber.log.Timber

@InternalCoroutinesApi
@HiltAndroidApp
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())


    }
}