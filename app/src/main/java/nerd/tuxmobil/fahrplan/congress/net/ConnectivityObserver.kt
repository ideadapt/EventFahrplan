@file:JvmName("ConnectivityObserver")

package nerd.tuxmobil.fahrplan.congress.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.support.v4.content.ContextCompat

/**
 * Observes network connectivity by consulting the [ConnectivityManager].
 * Observing can run infinitely or automatically be stopped after the first response is received.
 */
class ConnectivityObserver @JvmOverloads constructor(

        val context: Context,
        val onConnectionAvailable: () -> Unit,
        val onConnectionLost: () -> Unit = {},
        val shouldStopAfterFirstResponse: Boolean = false

) {

    private val connectivityManager
        get() = ContextCompat.getSystemService(context, ConnectivityManager::class.java)!!

    @Suppress("DEPRECATION")
    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    private val broadCastReceiver = object : BroadcastReceiver() {

        @Suppress("DEPRECATION")
        override fun onReceive(context: Context?, intent: Intent?) {
            if (ConnectivityManager.CONNECTIVITY_ACTION != intent?.action) {
                return
            }
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
                onConnectionAvailable.invoke()
            } else {
                onConnectionLost.invoke()
            }
            if (shouldStopAfterFirstResponse) {
                stop()
            }
        }

    }

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    onConnectionAvailable.invoke()
                    if (shouldStopAfterFirstResponse) {
                        stop()
                    }
                }

                override fun onLost(network: Network?) {
                    super.onLost(network)
                    onConnectionLost.invoke()
                    if (shouldStopAfterFirstResponse) {
                        stop()
                    }
                }
            }
        }
    }

    fun start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // Decouple from component lifecycle, use application context.
            // See: https://developer.android.com/reference/android/content/Context.html#getApplicationContext()
            context.applicationContext.registerReceiver(broadCastReceiver, intentFilter)
        } else {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    fun stop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            context.applicationContext.unregisterReceiver(broadCastReceiver)
        } else {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

}
