package org.jxxy.debug.sensors.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Insert()
    suspend fun insert(event: Event): Long

    @Query("select * from sensors_data_event where id = :id")
    suspend fun selectEventById(id: Long): Event

    @Query("select * from sensors_data_event where id > :id")
    suspend fun selectEvents(id: Long): List<Event>

    @Query("select max(id) from sensors_data_event")
    suspend fun selectMaxId(): Long
}
