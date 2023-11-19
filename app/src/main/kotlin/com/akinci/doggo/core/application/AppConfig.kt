package com.akinci.doggo.core.application

import com.akinci.doggo.BuildConfig
import javax.inject.Inject

/**
 * AppConfig is a wrapper class for BuildConfig fields which is mocked on unit tests.
 * **/
class AppConfig @Inject constructor() {
    fun isDebugMode() = BuildConfig.DEBUG

    fun getServiceEndpointBaseUrl() = BuildConfig.SERVICE_ENDPOINT_BASE_URL
}