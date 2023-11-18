package org.qiah.balabala.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import org.qiah.balabala.MyListener.AddNikkeListener
import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.R
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.adapter.ChatAdapter
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.adapter.SingleTypeAdapter
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.bean.ChatLeftImage
import org.qiah.balabala.bean.ChatLeftText
import org.qiah.balabala.bean.ChatRightImage
import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.bean.Message
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.ActivityChatModifyBinding
import org.qiah.balabala.databinding.ItemEmojiBinding
import org.qiah.balabala.databinding.ItemSelectNikkeBinding
import org.qiah.balabala.dialog.AddNikkeDialog
import org.qiah.balabala.util.GlideEngine
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.ResourceUtil
import org.qiah.balabala.util.SpanItemDecoration
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.hideKeyboard
import org.qiah.balabala.util.load
import org.qiah.balabala.util.show
import org.qiah.balabala.util.showKeyboard
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.viewHolder.MultipleViewHolder
import org.qiah.balabala.viewHolder.NikkeAvatarViewHolder
import org.qiah.balabala.viewHolder.SingleViewHolder

class ModifyChatActivity : BaseActivity<ActivityChatModifyBinding>() {
    private val TAG = "ModifyChatA"
    private lateinit var nikkes: ArrayList<Nikke>
    private lateinit var selectNikke: Nikke
    private var selectType: Int = 1
    private val db by lazy {
        MyDataBaseHelper(this, "Nikke_balabala", 1)
    }
    private lateinit var chat: Chat
    override fun initView() {
        val chat = intent.getSerializableExtra("chat") as? Chat
        chat?.let {
            this.chat = it
            view.chatRv.adapter = adapter
            init(it)
            loadClick()
        }
    }

    fun init(chat: Chat) {
        view.inputRcl.transitionToState(R.id.end)
        nikkes = chat.nikkes
        view.nameTv.text = chat.name
        avatarAdapter.add(Nikke("指挥官", "其它", ResourceUtil.getString(R.string.base_url) + ResourceUtil.getString(R.string.zhi)).also { selectNikke = it })
        view.avatarIv.load(selectNikke, true)
        avatarAdapter.add(nikkes)
        avatarAdapter.add(object : MultipleType {
            override fun viewType(): Int = MyType.ADD_NIKKE
        })
        view.selectNikkeCL.adapter = avatarAdapter
        view.selectNikkeCL.addItemDecoration(SpanItemDecoration(4F, 8F, 2, true))
        view.emojiRv.adapter = emojiAdapter
        val messages = db.selectAllMessageByChatId(chat.id)
        adapter.addMessages(messages)
    }
    fun loadClick() {
        view.selectNikkeCL.tag = false
        view.avatarIv.singleClick {
            val b = view.selectNikkeCL.tag as Boolean
            if (!b) {
                view.selectNikkeCL.show()
            } else {
                view.selectNikkeCL.gone()
            }
            view.selectNikkeCL.tag = !b
        }
        view.pictureIcon.tag = false
        view.pictureIcon.singleClick {
            val b = view.pictureIcon.tag as Boolean
            if (!b) {
                emojiAdapter.clear()
                emojiAdapter.add(ResourceUtil.getString(R.string.base_url) + "more.png")
                emojiAdapter.add(selectNikke.enoji)
                view.emojiRv.show()
                hideKeyboard(view.sendEt)
            } else {
                view.emojiRv.gone()
                showKeyboard(view.sendEt)
            }
            it.tag = !b
        }
//        view.backIcon.tag = false
        view.backIcon.singleClick {
            finish()
        }
        view.tpictureIcon.singleClick {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.TYPE_IMAGE)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        result?.let {
                                it1 ->
                            val s = result.get(0).realPath
                            if ("指挥官".equals(selectNikke.name)) {
                                val message = Message(
                                    0,
                                    chat.id,
                                    -1,
                                    4,
                                    s,
                                    adapter.itemCount
                                )
                                message.id = db.insertMessage(message)
                                adapter.add(ChatRightImage(s))
                            } else {
                                val message = Message(
                                    0,
                                    chat.id,
                                    selectNikke.id,
                                    4,
                                    s,
                                    adapter.itemCount
                                )
                                message.id = db.insertMessage(message)
                                adapter.add(ChatLeftImage(s, selectNikke))
                            }
                            view.chatRv.smoothScrollToPosition(adapter.itemCount - 1)
                            view.emojiRv.gone()
                        }
                    }
                    override fun onCancel() {
                    }
                })
        }
        view.sendEt.singleClick {
            view.emojiRv.gone()
        }
        view.tv1.tag = true
        view.tv2.tag = false
        view.tv3.tag = false
        view.tv1.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                view.tv2.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                view.tv3.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                it.background = ResourceUtil.getDrawable(R.drawable.bg_select_btn)
                it.setTextColor(ResourceUtil.getColor(R.color.common_text_black))
                view.tv2.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                view.tv3.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                selectType = 1
                it.tag = true
                view.tv2.tag = false
                view.tv3.tag = false
                view.pictureIcon.show()
                view.avatarIv.show()
                view.sendEt.setText("")
            }
        }
        view.tv2.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                view.emojiRv.gone()
                view.tv1.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                view.tv3.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                it.background = ResourceUtil.getDrawable(R.drawable.bg_select_btn)
                it.setTextColor(ResourceUtil.getColor(R.color.common_text_black))
                view.tv1.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                view.tv3.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                selectType = 2
                it.tag = true
                view.tv1.tag = false
                view.tv3.tag = false
                view.inputRcl.transitionToState(R.id.tureEnd)
            }
        }
        view.tv3.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                view.emojiRv.gone()
                view.tv2.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                view.tv1.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                it.background = ResourceUtil.getDrawable(R.drawable.bg_select_btn)
                it.setTextColor(ResourceUtil.getColor(R.color.common_text_black))
                view.tv2.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                view.tv1.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                it.tag = true
                view.tv2.tag = false
                selectType = 3
                view.tv1.tag = false
                view.inputRcl.transitionToState(R.id.tureEnd)
            }
        }
        view.sendBtn.singleClick {
            var s: String
            if (!view.sendEt.text.toString().also { s= it }.isNullOrEmpty()) {
                when (selectType) {
                    1 -> {
                        if ("指挥官".equals(selectNikke.name)) {
                            val message = Message(
                                0,
                                chat.id,
                                -1,
                                selectType,
                                s,
                                adapter.itemCount
                            )
                            message.id = db.insertMessage(message)
                            adapter.add(ChatRightText(s))
                            chat.news = s
                            db.updateChat(chat)
                        } else {
                            val message = Message(
                                0,
                                chat.id,
                                selectNikke.id,
                                selectType,
                                s,
                                adapter.itemCount
                            )
                            message.id = db.insertMessage(message)
                            adapter.add(ChatLeftText(s, selectNikke))
                            chat.news = s
                            db.updateChat(chat)
                        }
                    }
                    2 -> {
                        val message = Message(
                            0,
                            chat.id,
                            selectNikke.id,
                            selectType,
                            s,
                            adapter.itemCount
                        )
                        message.id = db.insertMessage(message)
                        adapter.add(object : MyType(s) {
                            override fun viewType(): Int = MyType.CHAT_NARRATION
                        })
                        view.inputRcl.transitionToState(R.id.tureEnd)
                    }
                    3 -> {
                        val message = Message(
                            0,
                            chat.id,
                            selectNikke.id,
                            selectType,
                            s,
                            adapter.itemCount
                        )
                        message.id = db.insertMessage(message)
                        adapter.add(object : MyType(s) {
                            override fun viewType(): Int = MyType.CHAT_SPLIT_LINE
                        })
                        view.inputRcl.transitionToState(R.id.tureEnd)
                    }
                }
                view.chatRv.smoothScrollToPosition(adapter.itemCount - 1)
                view.sendEt.setText("")
                hideKeyboard(view.sendEt)
                view.emojiRv.gone()
            }
        }
        view.sendEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null && !s.toString().isNullOrEmpty()) {
                    if (selectType == 1) view.inputRcl.transitionToState(R.id.start)
                } else {
                    if (selectType == 1) view.inputRcl.transitionToState(R.id.end)
                }
            }
        })
    }
    private val selectNikkeListener by lazy {
        object : SelectNikkeListener {
            override fun select(nikke: Nikke) {
                view.avatarIv.load(nikke.avatarPath, true)
                view.selectNikkeCL.gone()
                selectNikke = nikke
            }

            override fun unSelect(nikke: Nikke) {
                this.select(nikke)
            }
        }
    }
    private val adapter by lazy {
        ChatAdapter(db)
    }
    private val avatarAdapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.NIKKE -> NikkeAvatarViewHolder(ItemSelectNikkeBinding.inflate(layoutInflater, viewGroup, false), selectNikkeListener, false)
                MyType.ADD_NIKKE -> object : MultipleViewHolder<ItemSelectNikkeBinding, MultipleType>(ItemSelectNikkeBinding.inflate(layoutInflater, viewGroup, false)) {
                    override fun setHolder(entity: MultipleType) {
                        view.avatarIv.load(ResourceUtil.getString(R.string.base_url) + "more.png")
                        view.root.singleClick {
                            AddNikkeDialog(object : AddNikkeListener {
                                override fun onClick(nikkes: ArrayList<Nikke>) {
                                    val adapter = bindingAdapter as MultipleTypeAdapter
                                    adapter.insert(adapter.itemCount - 1, nikkes)
                                    chat.nikkes.addAll(nikkes)
                                    chat.nikkeIds.addAll(nikkes.map { it.id })
                                }
                            }).show(supportFragmentManager)
                        }
                    }
                }
                else -> null
            }
        }
    }
    private val emojiAdapter by lazy {
        object : SingleTypeAdapter<String>() {
            override fun createViewHolder(
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = object : SingleViewHolder<ItemEmojiBinding, String>(ItemEmojiBinding.inflate(layoutInflater, viewGroup, false)) {
                override fun setHolder(entity: String) {
                    val width = getWidth() / 5
                    view.root.layoutParams.width = width
                    view.root.layoutParams.height = width
                    view.emojiIv.load(entity, 6)
                }
            }
        }
    }
    override fun subscribeUi() {
    }
    override fun bindLayout(): ActivityChatModifyBinding = ActivityChatModifyBinding.inflate(layoutInflater)
}
