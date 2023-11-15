package org.qiah.balabala.activity

import android.util.Log
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.ActivityChatModifyBinding

class ModifyChatActivity : BaseActivity<ActivityChatModifyBinding>() {
    private val TAG = "ModifyChatA"
    override fun bindLayout(): ActivityChatModifyBinding = ActivityChatModifyBinding.inflate(layoutInflater)

    override fun initView() {
        var nikkes = intent.getSerializableExtra("nikkes") as ArrayList<Nikke>?
        nikkes?.let {
            Log.d(TAG, "initView: a" + nikkes)
        }
    }
    override fun subscribeUi() {
    }
}
