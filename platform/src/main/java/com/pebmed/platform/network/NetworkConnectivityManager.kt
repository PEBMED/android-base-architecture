package com.pebmed.platform.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import com.pebmed.platform.base.SingleLiveEvent

/**
 * Reference: https://gist.github.com/GokhanArik/b5c9ad58ce39218d722069d6cde34702
 */
class NetworkConnectivityManager(val context: Context) {
    private var isOnline = false
    private var connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var callback = ConnectionStatusCallback()

    private val _isConnectedEvent = SingleLiveEvent<Boolean>()
    val isConnectedEvent: SingleLiveEvent<Boolean>
        get() = _isConnectedEvent

    init {
        isOnline = getInitialConnectionStatus()
        sendEvent(isOnline)
        try {
            connectivityManager.unregisterNetworkCallback(callback)
        } catch (e: Exception) {
            Log.w(
                this.javaClass.name,
                "NetworkCallback for Wi-fi was not registered or already unregistered"
            )
        }
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
    }

    private fun getInitialConnectionStatus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo // Deprecated in 29
            activeNetwork != null && activeNetwork.isConnectedOrConnecting // // Deprecated in 28
        }
    }

    private fun sendEvent(status: Boolean) {
        _isConnectedEvent.postValue(status)
    }

    inner class ConnectionStatusCallback : ConnectivityManager.NetworkCallback() {

        private val activeNetworks: MutableList<Network> = mutableListOf()

        override fun onLost(network: Network) {
            super.onLost(network)
            activeNetworks.removeAll { activeNetwork -> activeNetwork == network }
            isOnline = activeNetworks.isNotEmpty()
            sendEvent(isOnline)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (activeNetworks.none { activeNetwork -> activeNetwork == network }) {
                activeNetworks.add(network)
            }
            isOnline = activeNetworks.isNotEmpty()
            sendEvent(isOnline)
        }
    }
}