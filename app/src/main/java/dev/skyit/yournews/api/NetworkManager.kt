package dev.skyit.yournews.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

interface INetworkManger {
    val isConnected: Boolean

    //suspend fun hasInternet() : Boolean //TODO for newer versions
}

class NetworkManager (
    private val context: Context
) : INetworkManger {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    override val isConnected: Boolean
        get() {
            return activeNetwork?.isConnectedOrConnecting == true
        }

}