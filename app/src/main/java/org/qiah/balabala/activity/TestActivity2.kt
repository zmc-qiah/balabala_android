package org.qiah.balabala.activity

import android.graphics.Color
import android.util.Log
import org.qiah.balabala.databinding.Activity1Binding
import org.qiah.balabala.util.singleClick

class TestActivity2 : BaseActivity<Activity1Binding>() {
    override fun bindLayout(): Activity1Binding = Activity1Binding.inflate(layoutInflater)

    override fun initView() {
        view.aIv.singleClick {
            val a = intent.getStringExtra("aa")
            Log.d("SensorsBind", "initView: aIv" + a)
        }
        view.aIv2.setOnClickListener { v ->
            Log.d("SensorsBind", "initView: aIv2")
        }
        view.aTv.setOnClickListener { v ->
            view.aTv.setTextColor(Color.BLUE)
            Log.d("SensorsBind", "initView: aTv")
        }
    }

    override fun subscribeUi() {
    }
}
