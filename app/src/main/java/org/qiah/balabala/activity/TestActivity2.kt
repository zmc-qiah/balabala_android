package org.qiah.balabala.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import org.qiah.balabala.databinding.Activity1Binding
import org.qiah.balabala.util.singleClick

class TestActivity2 : BaseActivity<Activity1Binding>() {
    private val TAG = "SensorDataManager"

    override fun bindLayout(): Activity1Binding = Activity1Binding.inflate(layoutInflater)

    override fun initView() {
        view.aIv.singleClick {
            Log.d(TAG, "initView: aIv")
        }
        view.aTv.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    Log.d(TAG, "initView: aTv")
                }
            },
        )
        view.aIv2.setOnClickListener {
            Log.d(TAG, "initView: aIv2")
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
