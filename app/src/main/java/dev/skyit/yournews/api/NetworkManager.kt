package dev.skyit.yournews.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlin.coroutines.CoroutineContext

interface INetworkManger {

    var isConnected: Boolean

    @InternalCoroutinesApi
    val hasInternet: Flow<Boolean>

}

class NetworkManager (
    private val context: Context
) : INetworkManger {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override var isConnected: Boolean = false

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override val hasInternet: Flow<Boolean> = callbackFlow {
        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                offer(true)
                isConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                offer(false)
                isConnected = false
            }
        })
        awaitClose() //crashes with channel closed if missing
    }



}