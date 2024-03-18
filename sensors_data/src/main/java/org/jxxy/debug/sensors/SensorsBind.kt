package org.jxxy.debug.sensors

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.room.Room
import org.jxxy.debug.maidian.R
import org.jxxy.debug.sensors.dataBase.Event
import org.jxxy.debug.sensors.dataBase.EventDataBase
import java.lang.ref.WeakReference
import kotlin.math.min

object SensorsBind {
    lateinit var app: Application
    private val TAG = "SensorsBind"
    private var flag = true
    private val appBackgroundDuration = 500L
    private lateinit var mDeviceInfo: Map<String, Any>
    private val hashMap: HashMap<Any, DataLabel> by lazy {
        HashMap()
    }
    private val lock = Any()
    private var nowActivity: WeakReference<Activity>? = null
    var isReflection: Boolean = true
    var isReflectionFragment: Boolean = false
    var isReflectionClick: Boolean = false
    var isReflectionDialog: Boolean = false
    var maxTextSize: Int = 20
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var mDeviceId: String

    private lateinit var sensorsBindHelper: SensorsBindHelper
    private lateinit var sensorDataManager: SensorDataManager

    fun init(app: Application) {
        synchronized(lock) {
            if (flag) {
                this.app = app
                sensorsBindHelper = SensorsBindHelper()
                val preferences =
                    app.getSharedPreferences(
                        app.resources.getString(app.applicationInfo.labelRes),
                        Context.MODE_PRIVATE,
                    )
                sensorsBindHelper.sharedPreferences =
                    preferences
                mDeviceId = sensorsBindHelper.getAndroidID(app.applicationContext)
                mDeviceInfo = sensorsBindHelper.getDeviceInfo(app.applicationContext)
                val db = Room.databaseBuilder(app.applicationContext, EventDataBase::class.java, "sensor_data").build()
                val dao = db.eventDao()
                sensorDataManager = SensorDataManager(preferences, dao, app.applicationContext)
                app.registerActivityLifecycleCallbacks(
                    object : Application.ActivityLifecycleCallbacks {
                        override fun onActivityCreated(
                            activity: Activity,
                            savedInstanceState: Bundle?,
                        ) {
                            val annotation = activity::class.java.getAnnotation(SensorsDataLabel::class.java)
                            val label = annotation?.value ?: ""
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
                            hashMap.remove(activity)
                        }
                    },
                )
                appStart(app, mDeviceId, mDeviceInfo)
                flag = false
            }
        }
    }

    private fun appStart(
        app: Application,
        mDeviceId: String,
        mDeviceInfo: Map<String, Any>,
    ) {
        val name = app.resources.getString(app.applicationInfo.labelRes)
        if (!sensorsBindHelper.checkEnd()) {
            sensorDataManager.onObtainData(
                Event(
                    Event.EVENT_APP_END,
                    name + "结束——补发",
                    mapOf(
                        "packName" to app.applicationInfo.packageName,
                        "className" to app::class.simpleName.toString(),
                        "deviceId" to mDeviceId,
                    ),
                ),
            )
        }
        sensorsBindHelper.setStart()
        sensorDataManager.onObtainData(
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
            sensorDataManager.onObtainData(
                Event(
                    Event.EVENT_APP_END,
                    name + "结束",
                    mapOf(
                        "packName" to app.applicationInfo.packageName,
                        "className" to app::class.simpleName.toString(),
                        "deviceId" to mDeviceId,
                    ),
                ),
            )
            sensorsBindHelper.setEnd()
        }

    private fun onActivityPaused(activity: Activity) {
        nowActivity?.clear()
        hashMap.getOrDefault(activity, null)?.let {
            val map =
                mutableMapOf<String, String>(
                    "packName" to it.packName,
                    "className" to it.className,
                    "deviceId" to mDeviceId,
                )
            if (it.label.length > 1) {
                map["label"] = it.label
            }
            sensorDataManager.onObtainData(
                Event(
                    Event.EVENT_ACTIVITY_STOP,
                    "${it.className} Paused",
                    map,
                ),
            )
        }
        handler.removeCallbacks(onAppEnd)
        handler.postDelayed(onAppEnd, appBackgroundDuration)
    }

    private fun onActivityResumed(activity: Activity) {
        nowActivity = WeakReference(activity)
        handler.removeCallbacks(onAppEnd)
        hashMap.getOrDefault(activity, null)?.let {
            sensorDataManager.onObtainData(
                Event(
                    Event.EVENT_ACTIVITY_RESUMED,
                    "${it.className} resumed",
                    mapOf<String, String>(
                        "label" to it.label,
                        "packName" to it.packName,
                        "className" to it.className,
                        "deviceId" to mDeviceId,
                    ),
                ),
            )
        }
    }

    @JvmStatic
    fun onTrackView(view: View?) {
        view?.let { view ->
            val context = view.context
            val resourceName = context.resources.getResourceName(if (view.id > 10) view.id else R.id.sensors_default_id)
            val name = "$resourceName be clicked"
            val type = Event.EVENT_VIEW_CLICK
            val map = mutableMapOf<String, String>()
            map["TAG"] = view.tag?.toString() ?: ""
            map["id"] = view.id.toString()
            if (isReflection || isReflectionClick) {
                getViewInfo(view, map)
            }
            a(type, name, map)
        }
    }

    private fun getViewInfo(
        view: View,
        map: MutableMap<String, String>,
    ) {
        val kClass = view::class.java
        map["class"] = kClass.simpleName.toString()
        if (view is TextView) {
            map["text"] = view.text.substring(0, min(view.text.length, maxTextSize))
        } else if (isReflectionClick) {
            val annotation = kClass.getAnnotation(SensorsViewLabelFiled::class.java)
            if (annotation != null) {
                val field = annotation.value
                val declaredField = kClass.getDeclaredField(field)
                val any = declaredField.get(view)?.toString() ?: ""
                map["label"] = "$field = $any"
            }
        }
    }

    fun a(
        type: Int,
        name: String,
        map: MutableMap<String, String>,
    ) {
        map["deviceId"] = mDeviceId
        nowActivity?.get()?.let {
            hashMap[it]?.let {
                map["activity"] = it.className
            }
        }
        sensorDataManager.onObtainData(
            Event(
                type,
                name,
                map,
            ),
        )
    }

    @JvmStatic
    fun onFragmentResume(fragment: Fragment) {
        val type = Event.EVENT_FRAGMENT_RESUMED
        val name = "fragment:${fragment::class.java.simpleName}可见"
        val map = HashMap<String, String>()
        if (isReflection || isReflectionFragment) {
            map["class"] = fragment::class.java.simpleName
            val dataLabel = fragment::class.java.getAnnotation(SensorsDataLabel::class.java)
            if (dataLabel != null) {
                map["label"] = dataLabel.value
            }
        }
        a(type, name, map)
    }

    @JvmStatic
    fun onFragmentPause(fragment: Fragment) {
        val type = Event.EVENT_FRAGMENT_PAUSE
        val simpleName = fragment::class.java.simpleName
        val name = "fragment:${simpleName}不可见"
        val map = HashMap<String, String>()
        if (isReflection || isReflectionFragment) {
            map["class"] = simpleName
            val dataLabel = fragment::class.java.getAnnotation(SensorsDataLabel::class.java)
            if (dataLabel != null) {
                map["label"] = dataLabel.value
            }
        }
        a(type, name, map)
    }

    @JvmStatic
    fun onDialogShow(
        dialog: DialogFragment,
        tag: String?,
    ) {
        val type = Event.EVENT_DIALOG_SHOW
        val simpleName = dialog::class.java.simpleName
        val name = "dialog:${simpleName}可见"
        val map = HashMap<String, String>()
        map["tag"] = tag ?: " "
        if (isReflection || isReflectionDialog) {
            map["class"] = simpleName
            val dataLabel = dialog::class.java.getAnnotation(SensorsDataLabel::class.java)
            if (dataLabel != null) {
                map["label"] = dataLabel.value
            }
        }
        a(type, name, map)
    }

    @JvmStatic
    fun onDialogDismiss(
        dialog: DialogFragment,
        tag: String?,
    ) {
        val type = Event.EVENT_DIALOG_DISSMISS
        val simpleName = dialog::class.java.simpleName
        val name = "dialog:${simpleName}不可见"
        val map = HashMap<String, String>()
        map["tag"] = tag ?: " "
        if (isReflection || isReflectionDialog) {
            map["class"] = simpleName
            val dataLabel = dialog::class.java.getAnnotation(SensorsDataLabel::class.java)
            if (dataLabel != null) {
                map["label"] = dataLabel.value
            }
        }
        a(type, name, map)
    }

    val ID = "NOWID"
}
