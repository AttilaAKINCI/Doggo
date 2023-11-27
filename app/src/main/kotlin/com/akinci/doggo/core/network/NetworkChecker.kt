package com.akinci.doggo.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val connectivityManager by lazy {
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    }

    private val _state: MutableStateFlow<Boolean> = MutableStateFlow(
        with(connectivityManager) {
            getNetworkCapabilities(activeNetwork)?.let { capability ->
                validTransportTypes.any { capability.hasTransport(it) }
            } ?: false
        }
    )
    val state = _state.asStateFlow()

    fun isConnected() = _state.value

    private val networkStateCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _state.value = true
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            _state.value = false
        }
    }

    init {
        // subscribe on init
        val request = NetworkRequest.Builder().apply {
            validTransportTypes.onEach { addTransportType(it) }
        }

        connectivityManager.registerNetworkCallback(request.build(), networkStateCallback)
    }

    companion object {
        val validTransportTypes = listOf(
            NetworkCapabilities.TRANSPORT_WIFI,
            NetworkCapabilities.TRANSPORT_CELLULAR
        )
    }
}
