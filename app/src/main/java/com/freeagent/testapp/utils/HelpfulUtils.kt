package com.freeagent.testapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class HelpfulUtils {

    companion object {
        fun hasNetwork(context: Context): Boolean? {
            var isConnected: Boolean? = false // Initial Value
            try {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                if (activeNetwork != null && activeNetwork.isConnected)
                    isConnected = true
            } catch (e: Throwable) {
                e.printStackTrace()

            }
            return isConnected
        }

    }

}