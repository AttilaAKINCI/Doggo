package com.akinci.doggoapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {
    // I replaced default AndroidJUnitRunner with HiltTestRunner in order to provide Hilt
    // injections and managements
    // I pass HiltTestRunner as testInstrumentationRunner in build.gradle in default configs
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}