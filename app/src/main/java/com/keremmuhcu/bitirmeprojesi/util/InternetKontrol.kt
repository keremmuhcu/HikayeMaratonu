package com.keremmuhcu.bitirmeprojesi.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object InternetKontrol {
    fun internetVarMi(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}