package org.jxxy.debug.sensors

import java.text.SimpleDateFormat
import java.util.Date

class Event(val type: Int, val name: String, val eventMap: Map<String, String>) {
    val time: String

    init {
        val date = Date(System.currentTimeMillis())
        time = formatter.format(date)
    }

    companion object {
        final val formatter = SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒")
        final const val EVENT_APP_START = 1022
        final const val EVENT_APP_END = 1023
        final const val EVENT_ACTIVITY_RESUMED = 1024
        final const val EVENT_ACTIVITY_STOP = 1025
        final const val EVENT_FRAGMENT_RESUMED = 1026
        final const val EVENT_FRAGMENT_PAUSE = 1027
        final const val EVENT_DIALOG_SHOW = 1028
        final const val EVENT_DIALOG_DISSMISS = 1029
        final const val EVENT_VIEW_CLICK = 1029
    }

    override fun toString(): String {
        return "Event(type=$type, name='$name', eventMap=$eventMap, time='$time')"
    }
}
