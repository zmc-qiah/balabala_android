package org.jxxy.debug.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date

class SensorsBindHelper {
    var sharedPreferences: SharedPreferences ? = null
    private val KEY_END = "key_end"
    fun getDeviceInfo(context: Context): Map<String, Any> {
        val deviceInfo: MutableMap<String, Any> = HashMap()
        deviceInfo["\$os"] = "Android"
        deviceInfo["\$os_version"] =
            if (Build.VERSION.RELEASE == null) "UNKNOWN" else Build.VERSION.RELEASE
        deviceInfo["\$manufacturer"] =
            if (Build.MANUFACTURER == null) "UNKNOWN" else Build.MANUFACTURER
        if (TextUtils.isEmpty(Build.MODEL)) {
            deviceInfo["\$model"] = "UNKNOWN"
        } else {
            deviceInfo["\$model"] = Build.MODEL.trim { it <= ' ' }
        }
        try {
            val manager = context.packageManager
            val packageInfo =
                manager.getPackageInfo(context.packageName, 0)
            deviceInfo["\$app_version"] = packageInfo.versionName
            val labelRes = packageInfo.applicationInfo.labelRes
            deviceInfo["\$app_name"] = context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val displayMetrics = context.resources.displayMetrics
        deviceInfo["\$screen_height"] = displayMetrics.heightPixels
        deviceInfo["\$screen_width"] = displayMetrics.widthPixels
        return Collections.unmodifiableMap(deviceInfo)
    }

    @SuppressLint("HardwareIds")
    fun getAndroidID(mContext: Context): String {
        var androidID = ""
        try {
            androidID =
                Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return androidID
    }
    fun getNowTime(): String {
        val formatter = SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒")
        val date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }
    fun checkEnd(): Boolean {
        // -1表示没有这个值
        // 0表示没有正常退出
        // 1表示正常退出
        var res = false
        val int = sharedPreferences?.getInt(KEY_END, -1)
        Log.d("SensorsBind", "checkEnd: " + int)
        when (int) {
            -1 -> {
                res = true
                sharedPreferences?.edit()?.let {
                    it.putInt(KEY_END, 0)
                    it.apply()
                }
            }
            0 -> {
                res = false
            }
            1 -> {
                res = true
            }
        }
        return res
    }
    fun setEnd() {
        sharedPreferences?.edit()?.let {
            it.putInt(KEY_END, 1)
            it.apply()
        }
    }
    fun setStart() {
        sharedPreferences?.edit()?.let {
            it.putInt(KEY_END, 0)
            it.apply()
        }
    }
}
