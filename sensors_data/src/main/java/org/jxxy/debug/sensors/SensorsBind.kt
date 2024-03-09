package org.jxxy.debug.sensors

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

object SensorsBind {
    private lateinit var app: Application
    private val TAG = "SensorsBind"
    private val appBackgroundDuration = 500L
    private lateinit var mDeviceInfo: Map<String, Any>
    private val hashMap: HashMap<Any, DataLabel> by lazy {
        HashMap()
    }
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var mDeviceId: String

    private lateinit var sensorsBindHelper: SensorsBindHelper

    fun init(app: Application) {
        this.app = app
        sensorsBindHelper = SensorsBindHelper()
        sensorsBindHelper.sharedPreferences = app.getSharedPreferences(app.resources.getString(app.applicationInfo.labelRes), Context.MODE_PRIVATE)
        mDeviceId = sensorsBindHelper.getAndroidID(app.getApplicationContext())
        mDeviceInfo = sensorsBindHelper.getDeviceInfo(app.getApplicationContext())
        app.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    val annotation = activity::class.java.getAnnotation(SensorsDataLabel::class.java)
                    val label = if (annotation == null) " " else annotation.value
                    val packName = activity.packageManager.getActivityInfo(activity.componentName, 0).packageName.toString()
                    val string = activity::class.simpleName.toString()
                    hashMap.put(activity, DataLabel(label, string, packName))
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                    this@SensorsBind.onActivityResumed(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    this@SensorsBind.onActivityPaused(activity)
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
                ) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }
            }
        )
        appStart(app, mDeviceId, mDeviceInfo)
    }

    private fun appStart(app: Application, mDeviceId: String, mDeviceInfo: Map<String, Any>) {
        val name = app.resources.getString(app.applicationInfo.labelRes)
        if (!sensorsBindHelper.checkEnd()) {
            onObtainData(
                Event(
                    Event.EVENT_APP_START,
                    name + "结束——补发",
                    mapOf(
                        "packName" to app.applicationInfo.packageName,
                        "className" to app::class.simpleName.toString(),
                        "deviceId" to mDeviceId,
                        "deviceInfo" to mDeviceInfo.toString()
                    )
                )
            )
        }
        sensorsBindHelper.setStart()
        onObtainData(
            Event(
                Event.EVENT_APP_START,
                name + "启动",
                mapOf(
                    "packName" to app.applicationInfo.packageName,
                    "className" to app::class.simpleName.toString(),
                    "deviceId" to mDeviceId,
                    "deviceInfo" to mDeviceInfo.toString()
                )
            )
        )
    }
    private val onAppEnd = object : Runnable {
        override fun run() {
            val name = app.resources.getString(app.applicationInfo.labelRes)
            onObtainData(
                Event(
                    Event.EVENT_APP_START,
                    name + "结束",
                    mapOf(
                        "packName" to app.applicationInfo.packageName,
                        "className" to app::class.simpleName.toString(),
                        "deviceId" to mDeviceId,
                        "deviceInfo" to mDeviceInfo.toString()
                    )
                )
            )
            sensorsBindHelper.setEnd()
        }
    }
    private fun onActivityPaused(activity: Activity) {
        hashMap.getOrDefault(activity, null)?.let {
            onObtainData(
                Event(
                    Event.EVENT_ACTIVITY_STOP,
                    "${it.className} stopped",
                    mapOf<String, String>(
                        "label" to it.label,
                        "packName" to it.packName,
                        "className" to it.className,
                        "deviceId" to mDeviceId,
                        "deviceInfo" to mDeviceInfo.toString()
                    )
                )
            )
        }
        handler.removeCallbacks(onAppEnd)
        handler.postDelayed(onAppEnd, appBackgroundDuration)
    }

    private fun onActivityResumed(activity: Activity) {
        handler.removeCallbacks(onAppEnd)
        hashMap.getOrDefault(activity, null)?.let {
            onObtainData(
                Event(
                    Event.EVENT_ACTIVITY_STOP,
                    "${it.className} resumed",
                    mapOf<String, String>(
                        "label" to it.label,
                        "packName" to it.packName,
                        "className" to it.className,
                        "deviceId" to mDeviceId,
                        "deviceInfo" to mDeviceInfo.toString()
                    )
                )
            )
        }
    }
    private fun onObtainData(event: Event) {
        Log.d(TAG, "onObtainData: event: $event")
    }
}
