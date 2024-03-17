package org.jxxy.debug.sensors

import android.util.Log

object SensorDataManager {
    private val TAG = "SensorsBind"
    fun onObtainData(event: Event) {
        Log.d(TAG, "onObtainData: event: $event")
    }
}