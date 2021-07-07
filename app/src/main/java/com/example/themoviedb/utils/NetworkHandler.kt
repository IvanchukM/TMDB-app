package com.example.themoviedb.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

private const val HOSTNAME = "8.8.8.8"
private const val PORT = 53
private const val TIMEOUT = 1500

@Singleton
class NetworkHandler @Inject constructor(context: Context) :
    ConnectivityManager.NetworkCallback() {

    private val subject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    init {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), this)
        }

        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }
        subject.onNext(isConnected)
    }

    fun observeNetworkState(): Observable<Boolean> = subject

    override fun onAvailable(network: Network) {
        subject.onNext(doesNetworkHasInternet())
    }

    override fun onLost(network: Network) {
        subject.onNext(false)

    }

    private fun doesNetworkHasInternet(): Boolean {
        return try {
            Log.d("TAG", "Pinging goggle")
            val socket = Socket()
            socket.connect(InetSocketAddress(HOSTNAME, PORT), TIMEOUT)
            socket.close()
            Log.d("TAG", "Ping success")
            true
        } catch (e: IOException) {
            Log.d("TAG", "No internet connection")
            false
        }
    }
}
