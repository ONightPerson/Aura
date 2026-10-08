package com.lbz.aura.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepository(context: Context) {
    companion object {
        private const val TAG = "LocationRepository"
    }

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Pair<Double, Double>> = callbackFlow {
        // 1. 尝试获取最后一次已知位置
        val lastGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val lastNetworkLocation =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val bestLocation = when {
            lastGpsLocation != null && lastNetworkLocation != null -> {
                if (lastGpsLocation.time > lastNetworkLocation.time) lastGpsLocation else lastNetworkLocation
            }

            else -> lastGpsLocation ?: lastNetworkLocation
        }
        bestLocation?.let {
            trySend(it.latitude to it.longitude)
        }

        // 2. 注册实时更新监听
        val listener = LocationListener { location ->
            Log.i(TAG, "getLocationUpdates: location: $location")
            trySend(location.latitude to location.longitude)
        }

        val providers = locationManager.getProviders(true)
        Log.i(TAG, "location providers: $providers")

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            Log.i(TAG, "getLocationUpdates: gps")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1f,
                listener,
                Looper.getMainLooper()
            )
        }

        awaitClose {
            locationManager.removeUpdates(listener)
        }
    }
}
