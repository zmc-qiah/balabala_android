package org.jxxy.debug.sensors.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensors_data_event")
class Event(
    @ColumnInfo
    var type: Int,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var eventMap: Map<String, String>,
) {
    var time: Long

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    init {
        time = System.currentTimeMillis()
    }

    companion object {
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
        return "Event(type=$type, name='$name', eventMap=$eventMap, time='$time', id=$id)"
    }
}
