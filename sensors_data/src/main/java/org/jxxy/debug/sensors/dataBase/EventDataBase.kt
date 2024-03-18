package org.jxxy.debug.sensors.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(MapConverters::class)
@Database(entities = arrayOf(Event::class), version = 1, exportSchema = false)
abstract class EventDataBase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
