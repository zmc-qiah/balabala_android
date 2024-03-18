package org.qiah.balabala.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.qiah.balabala.Bean
import org.qiah.balabala.databinding.Activity1Binding
import org.qiah.balabala.util.singleClick
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TestActivity2 : BaseActivity<Activity1Binding>() {
    private val gson = Gson()

    override fun bindLayout(): Activity1Binding = Activity1Binding.inflate(layoutInflater)

    override fun initView() {
        view.aIv.singleClick {
            GlobalScope.launch(Dispatchers.IO) {
                // 创建URL对象
                // 创建URL对象
                val url = URL("http://120.27.202.206:8081/user/login")
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestMethod("POST")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setDoOutput(true)
                val jsonInputString = gson.toJson(Bean("application", "1234"))
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
                    Log.d("initView: ", "Response: $response")
                }
                connection.disconnect()
            }
        }
    }

    fun isOnWifi(): Boolean {
        var res = false
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        if (networkInfo?.isConnected == true) {
            for (network in connMgr.allNetworks) {
                if (connMgr.getNetworkInfo(network)?.type == ConnectivityManager.TYPE_WIFI) {
                    res = true
                    break
                }
            }
        }
        return res
    }

    override fun subscribeUi() {
    }
}
