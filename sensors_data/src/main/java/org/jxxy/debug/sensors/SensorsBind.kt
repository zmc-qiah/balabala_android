package org.jxxy.debug.sensors

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.jxxy.debug.maidian.R

object SensorsBind {
    lateinit var app: Application
    private val TAG = "SensorsBind"
    private val appBackgroundDuration = 500L
    private lateinit var mDeviceInfo: Map<String, Any>
    private val hashMap: HashMap<Any, DataLabel> by lazy {
        HashMap()
    }
    var isReflection: Boolean = true
    var isReflectionActivity: Boolean = false
    var isReflectionFragment: Boolean = false
    var isReflectionClick: Boolean = false
    var isReflectionDialog: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var mDeviceId: String

    private lateinit var sensorsBindHelper: SensorsBindHelper

    fun init(app: Application)  {
        this.app = app
        sensorsBindHelper = SensorsBindHelper()
        sensorsBindHelper.sharedPreferences =
            app.getSharedPreferences(
                app.resources.getString(app.applicationInfo.labelRes),
                Context.MODE_PRIVATE,
            )
        mDeviceId = sensorsBindHelper.getAndroidID(app.applicationContext)
        mDeviceInfo = sensorsBindHelper.getDeviceInfo(app.applicationContext)
        app.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {
                    val annotation = activity::class.java.getAnnotation(SensorsDataLabel::class.java)
                    val label = annotation?.value ?: " "
                    val packName = activity.packageManager.getActivityInfo(activity.componentName, 0).packageName.toString()
                    val string = activity::class.simpleName.toString()
                    hashMap.put(activity, DataLabel(label, string, packName))
//                    activity.window.callback = SensorsWindowCallback(activity, activity.window.callback)
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
                    outState: Bundle,
                ) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }
            },
        )
        appStart(app, mDeviceId, mDeviceInfo)
    }

    private fun appStart(
        app: Application,
        mDeviceId: String,
        mDeviceInfo: Map<String, Any>,
    ) {
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
                        "deviceInfo" to mDeviceInfo.toString(),
                    ),
                ),
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
                    "deviceInfo" to mDeviceInfo.toString(),
                ),
            ),
        )
    }

    private val onAppEnd =
        Runnable {
            val name = app.resources.getString(app.applicationInfo.labelRes)
            onObtainData(
                Event(
                    Event.EVENT_APP_START,
                    name + "结束",
                    mapOf(
                        "packName" to app.applicationInfo.packageName,
                        "className" to app::class.simpleName.toString(),
                        "deviceId" to mDeviceId,
                        "deviceInfo" to mDeviceInfo.toString(),
                    ),
                ),
            )
            sensorsBindHelper.setEnd()
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
                        "deviceInfo" to mDeviceInfo.toString(),
                    ),
                ),
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
                        "deviceInfo" to mDeviceInfo.toString(),
                    ),
                ),
            )
        }
    }

    private fun onObtainData(event: Event) {
        Log.d(TAG, "onObtainData: event: $event")
    }

    @JvmStatic
    fun onTrackView(view: View?) {
        view?.let {
            val context = app.baseContext
            val resourceName = context.resources.getResourceName(if (it.id > 10) it.id else R.id.sensors_default_id)
            onObtainData(
                Event(
                    Event.EVENT_ACTIVITY_STOP,
                    "$resourceName be clicked",
                    mapOf(
                        "view_id" to resourceName,
                        "className" to view::class.simpleName.toString(),
                        "deviceId" to mDeviceId,
                        "deviceInfo" to mDeviceInfo.toString(),
                    ),
                ),
            )
        }
    }

    @JvmStatic
    fun onFragmentResume(fragment: Fragment) {
        Log.d(TAG, "onFragmentResume: " + if (fragment.activity != null) fragment.activity!!::class.simpleName.toString() else "无")
    }

    @JvmStatic
    fun onFragmentPause(fragment: Fragment) {
        Log.d(TAG, "onFragmentPause: ")
    }

    @JvmStatic
    fun onDialogShow(
        dialog: DialogFragment,
        tag: String?,
    ) {
        Log.d(TAG, "onDialogShow: ")
    }

    @JvmStatic
    fun onDialogDismiss(
        dialog: DialogFragment,
        tag: String?,
    ) {
        Log.d(TAG, "onDialogDismiss: ")
    }
}
