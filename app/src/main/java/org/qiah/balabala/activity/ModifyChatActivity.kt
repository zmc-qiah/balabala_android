package org.qiah.balabala.activity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.R
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.ChatLeftImage
import org.qiah.balabala.bean.ChatLeftText
import org.qiah.balabala.bean.ChatRightImage
import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.bean.CreateChat
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.ActivityChatModifyBinding
import org.qiah.balabala.databinding.ItemChatLeftImageBinding
import org.qiah.balabala.databinding.ItemChatLeftTextBinding
import org.qiah.balabala.databinding.ItemChatRightImageBinding
import org.qiah.balabala.databinding.ItemChatRightTextBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.show
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.viewHolder.ChatLeftImageViewHolder
import org.qiah.balabala.viewHolder.ChatLeftTextViewHolder
import org.qiah.balabala.viewHolder.ChatRightImageViewHolder
import org.qiah.balabala.viewHolder.ChatRightTextViewHolder

class ModifyChatActivity : BaseActivity<ActivityChatModifyBinding>() {
    private val TAG = "ModifyChatA"
    private val adapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.CHAT_LEFT_TEXT -> ChatLeftTextViewHolder(ItemChatLeftTextBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_RIGHT_TEXT -> ChatRightTextViewHolder(ItemChatRightTextBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_LEFT_IMAGE -> ChatLeftImageViewHolder(ItemChatLeftImageBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_RIGHT_IMAGE -> ChatRightImageViewHolder(ItemChatRightImageBinding.inflate(layoutInflater, viewGroup, false))
                else -> null
            }
        }
    }
    override fun bindLayout(): ActivityChatModifyBinding = ActivityChatModifyBinding.inflate(layoutInflater)

    override fun initView() {
        val nikkes = intent.getSerializableExtra("nikkes") as? CreateChat
        nikkes?.let {
            Log.d(TAG, "initView: a" + nikkes)
            view.chatRv.adapter = adapter
            adapter.add(ChatLeftText("aaaaaa", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftText("aaaaaa"))
            adapter.add(ChatRightText("bbbbsajsaksjasa"))
            adapter.add(ChatRightImage("https://i0.hdslb.com/bfs/archive/c1804552a49963d9c5c6be8d4d285e4ce12ad8d7.jpg@472w_264h_1c_!web-dynamic.webp"))
            adapter.add(ChatLeftImage("https://album.biliimg.com/bfs/new_dyn/15293a912adc0f2d9ed60ffd4bfee754692529506.jpg@1048w_!web-dynamic.webp", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftImage("https://i0.hdslb.com/bfs/new_dyn/a86a7c0548d36d0a3b1fa5bf1a2ef64c336132667.png@1048w_!web-dynamic.webp"))
            adapter.add(ChatLeftText("aaaaaa", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftText("aaaaaa"))
            adapter.add(ChatRightText("bbbbsajsaksjasa"))
            adapter.add(ChatRightImage("https://i0.hdslb.com/bfs/archive/c1804552a49963d9c5c6be8d4d285e4ce12ad8d7.jpg@472w_264h_1c_!web-dynamic.webp"))
            adapter.add(ChatLeftImage("https://album.biliimg.com/bfs/new_dyn/15293a912adc0f2d9ed60ffd4bfee754692529506.jpg@1048w_!web-dynamic.webp", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftImage("https://i0.hdslb.com/bfs/new_dyn/a86a7c0548d36d0a3b1fa5bf1a2ef64c336132667.png@1048w_!web-dynamic.webp"))
            view.nameTv.text = nikkes.name
            loadClick()
        }
    }
    override fun subscribeUi() {
    }

    fun loadClick() {
        view.avatarIv.tag = false
        view.avatarIv.singleClick {
            val b = view.avatarIv.tag as Boolean
            if (!b) {
                view.selectNikkeCL.show()
            } else {
                view.selectNikkeCL.gone()
            }
        }
        view.pictureIcon.tag = false
        view.pictureIcon.singleClick {
            val b = view.pictureIcon.tag as Boolean
            if (!b) {
            } else {
            }
        }
    }
}
