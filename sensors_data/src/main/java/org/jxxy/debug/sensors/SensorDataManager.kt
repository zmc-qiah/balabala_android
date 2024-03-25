package org.jxxy.debug.sensors

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jxxy.debug.sensors.dataBase.Event
import org.jxxy.debug.sensors.dataBase.EventDao
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SensorDataManager(val preferences: SharedPreferences? = null, val dao: EventDao) {
    private val TAG = "SensorDataManager"
    private var gson: Gson = Gson()
    private val mutex = Mutex()

    fun onObtainData(
        event: Event,
        isOnWifi: Boolean,
    ) {
        Log.d(TAG, "onObtainData: " + event)
        runBlocking(Dispatchers.IO) {
            async {
                event.id = dao.insert(event)
            }.await()
            mutex.withLock {
                if (isOnWifi) {
                    val target = dao.selectMaxId()
                    val nowId = preferences?.getLong(SensorsBind.ID, 0L) ?: 0L
                    val code =
                        async {
                            if (target == nowId + 1) {
                                sendData(event)
                            } else {
                                val events = dao?.selectEvents(nowId)
                                if (events != null && events.size > 0) {
                                    sendData(events)
                                } else {
                                    sendData(event)
                                }
                            }
                        }.await()
                    if (code == 200) {
                        preferences?.let {
                            val edit = it.edit()
                            edit.putLong(SensorsBind.ID, target)
                            edit.apply()
                        }
                    }
                }
            }
        }
    }

    fun sendData(events: List<Event>): Int {
        val url = URL("http://120.27.202.206:8081/user/login")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestMethod("POST")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setDoOutput(true)
        val jsonInputString = gson.toJson(events)
        val input =
            jsonInputString.toByteArray(charset("utf-8"))
        connection.outputStream.write(input, 0, input.size)
        val code = connection.responseCode
        BufferedReader(InputStreamReader(connection.inputStream, "utf-8")).use { br ->
            val response = StringBuilder()
            var responseLine: String = ""
            while ((br.readLine())?.also { it -> responseLine = it } != null) {
                response.append(responseLine.trim { it <= ' ' })
            }
        }
        connection.disconnect()
        return code
    }

    fun sendData(event: Event): Int {
        val url = URL("http://120.27.202.206:8081/user/login")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestMethod("POST")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setDoOutput(true)
        val jsonInputString = gson.toJson(event)
        val input =
            jsonInputString.toByteArray(charset("utf-8"))
        connection.outputStream.write(input, 0, input.size)
        val code = connection.responseCode
        BufferedReader(InputStreamReader(connection.inputStream, "utf-8")).use { br ->
            val response = StringBuilder()
            var responseLine: String = ""
            while ((br.readLine())?.also { it -> responseLine = it } != null) {
                response.append(responseLine.trim { it <= ' ' })
            }
        }
        connection.disconnect()
        return code
    }
}
