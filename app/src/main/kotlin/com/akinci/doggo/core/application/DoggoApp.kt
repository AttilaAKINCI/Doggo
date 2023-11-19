package com.akinci.doggo.core.application

import android.app.Application
import com.akinci.doggo.core.logger.LoggerInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DoggoApp : Application() {
    @Inject
    lateinit var loggerInitializer: LoggerInitializer

    override fun onCreate() {
        super.onCreate()

        // initialize timber trees
        loggerInitializer.initialize()
    }
}