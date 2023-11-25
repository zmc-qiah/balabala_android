package org.qiah.balabala.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import org.qiah.balabala.MyListener.AddNikkeListener
import org.qiah.balabala.MyListener.ClickItemListener
import org.qiah.balabala.MyListener.ClickModifyDialogListener
import org.qiah.balabala.MyListener.ClickPreviewEmojiDialogListener
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.R
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.adapter.ChatAdapter
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.bean.ChatItem
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
import org.qiah.balabala.dialog.PreviewEmojiDialog
import org.qiah.balabala.util.CommonItemDecoration
import org.qiah.balabala.util.GlideEngine
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.ResourceUtil
import org.qiah.balabala.util.SpanItemDecoration
import org.qiah.balabala.util.dp
import org.qiah.balabala.util.enen
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.hideKeyboard
import org.qiah.balabala.util.load
import org.qiah.balabala.util.nullOrNot
import org.qiah.balabala.util.show
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.util.toast
import org.qiah.balabala.viewHolder.EmojiViewHolder
import org.qiah.balabala.viewHolder.MultipleViewHolder
import org.qiah.balabala.viewHolder.NikkeAvatarViewHolder

class ModifyChatActivity : BaseActivity<ActivityChatModifyBinding>() {
    private val TAG = "ModifyChatA"
    private lateinit var nikkes: ArrayList<Nikke>
    private lateinit var selectNikke: Nikke
    private var selectType: Int = 1
    private val db by lazy {
        MyDataBaseHelper(this, "Nikke_balabala", 1)
    }
    private lateinit var chat: Chat
    private var isModify: Boolean = false
    private var isInsert: Boolean = false
    private var modifyPosition: Int = 0
    private lateinit var modifyCI: ChatItem
    override fun initView() {
        (intent.getSerializableExtra("chat") as? org.qiah.balabala.bean.Chat)?.let {
            db.selectChatById(it.id)?.let {
                this.chat = it
                view.chatRv.adapter = adapter
                init(it)
                loadClick()
            }
        }
    }
    fun init(chat: Chat) {
        view.inputRcl.transitionToState(R.id.end)
        if (chat.tempIds.isNullOrEmpty() && chat.nikkes != null) {
            nikkes = chat.nikkes!!
        } else {
            Log.d(TAG, "init: " + chat.tempIds)
            val ids = gson.fromJson<ArrayList<Int>>(
                chat.tempIds,
                object : TypeToken<ArrayList<Int>>() {}.type
            )
            nikkes = db.selectNikkeByIds(ids)
            chat.nikkeIds = ids
            chat.nikkes = nikkes
        }
        view.nameTv.text = chat.name
        avatarAdapter.add(db.selectNikkeById(0).also { selectNikke = it!! })
        view.avatarIv.load(selectNikke, true)
        avatarAdapter.add(nikkes)
        avatarAdapter.add(object : MultipleType {
            override fun viewType(): Int = MyType.ADD_NIKKE
        })
        view.selectNikkeCL.adapter = avatarAdapter
        view.selectNikkeCL.addItemDecoration(SpanItemDecoration(4F, 8F, 2, true))
        view.emojiRv.adapter = emojiAdapter
        val messages = db.selectAllMessageByChatId(chat.id)
        view.chatRv.addItemDecoration(CommonItemDecoration(8.dp()))
        adapter.addMessages(messages)
        view.thingIv.load(R.drawable.btn_bg, 200)
    }
    fun loadClick() {
        view.thingIv.singleClick {
            if (selectNikke.id != 0) {
                val s = selectNikke.name
                val message = Message(
                    0,
                    chat.id,
                    selectNikke.id,
                    5,
                    s,
                    if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                )
                if (!isModify) {
                    message.id = db.insertMessage(message)
                } else {
                    message.id = modifyCI.message.id
                    Log.d("addMessages", "onResult: " + message.postion + message.postion)
                    db.updateMessage(message)
                }
                if (!isModify && !isInsert) {
                    adapter.add(object : ChatItem(message, s) {
                        override fun viewType(): Int = MyType.CHAT_THING
                    })
                } else if (isInsert) {
                    adapter.insertMessage(object : ChatItem(message, s) {
                        override fun viewType(): Int = MyType.CHAT_THING
                    })
                } else {
                    adapter.updateMessage(object : ChatItem(message, s) {
                        override fun viewType(): Int = MyType.CHAT_THING
                    })
                }
                view.chatRv.smoothScrollToPosition(if (!isModify && !isInsert) adapter.itemCount else modifyPosition + 1)
            } else {
                "没有和自己的事件".toast()
            }
        }
        view.selectNikkeCL.tag = false
        view.avatarIv.singleClick {
            hideEmoji()
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
            hideEmoji()
            val b = view.pictureIcon.tag as Boolean
            if (!b) {
                emojiAdapter.clear()
                emojiAdapter.add(object : MyType(ResourceUtil.getString(R.string.base_url) + "more.png") {
                    override fun viewType(): Int = MyType.ADD_EMOJI
                })
                var list: List<MyType>? = null
                selectNikke.enoji.nullOrNot(
                    {
                        Log.d(TAG, "loadClick: exception " + selectNikke.tememoji)
                        if (!selectNikke.tememoji.isNullOrEmpty()) {
                            var list1 = gson.fromJson<ArrayList<String>>(
                                selectNikke.tememoji,
                                object : TypeToken<ArrayList<String>>() {}.type
                            )
                            selectNikke.enoji = list1
                            list = list1.map {
                                object : MyType(it) {
                                    override fun viewType(): Int = MyType.EMOJI
                                }
                            }
                        }
                    },
                    {
                        list = it.map {
                            object : MyType(it) {
                                override fun viewType(): Int = MyType.EMOJI
                            }
                        }
                    }
                )
                emojiAdapter.add(list)
                view.emojiRv.show()
                hideKeyboard(view.sendEt)
            } else {
                hideEmoji()
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
                .isGif(true)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        result?.let {
                                it1 ->
                            val s = result.get(0).realPath
                            if ("指挥官".equals(selectNikke.name)) {
                                val message = Message(
                                    0,
                                    chat.id,
                                    0,
                                    4,
                                    s,
                                    if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                                )
                                if (!isModify) {
                                    message.id = db.insertMessage(message)
                                } else {
                                    message.id = modifyCI.message.id
                                    Log.d("addMessages", "onResult: " + message.postion + message.postion)
                                    db.updateMessage(message)
                                }
                                if (!isModify && !isInsert) {
                                    adapter.add(ChatRightImage(message, s))
                                } else if (isInsert) {
                                    adapter.insertMessage(ChatRightImage(message, s))
                                } else {
                                    adapter.updateMessage(ChatRightImage(message, s))
                                }
                            } else {
                                val message = Message(
                                    0,
                                    chat.id,
                                    selectNikke.id,
                                    4,
                                    s,
                                    if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                                )
                                if (!isModify) {
                                    message.id = db.insertMessage(message)
                                } else {
                                    message.id = modifyCI.message.id
                                    db.updateMessage(message)
                                }
                                if (!isModify && !isInsert) {
                                    adapter.add(ChatLeftImage(message, s, selectNikke))
                                } else if (isInsert) {
                                    adapter.insertMessage(ChatLeftImage(message, s, selectNikke))
                                } else {
                                    adapter.updateMessage(ChatLeftImage(message, s, selectNikke))
                                }
                            }
                            view.chatRv.smoothScrollToPosition(if (!isModify && !isInsert) adapter.itemCount - 1 else modifyPosition + 1)
                            hideEmoji()
                            isModify = false
                            isInsert = false
                        }
                    }
                    override fun onCancel() {
                    }
                })
        }
        view.sendEt.singleClick {
            hideEmoji()
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
                hideEmoji()
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
                hideEmoji()
                view.inputRcl.transitionToState(R.id.tureEnd)
            }
        }
        view.tv3.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                hideEmoji()
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
                hideEmoji()
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
                                0,
                                selectType,
                                s,
                                if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                            )
                            if (!isModify) {
                                message.id = db.insertMessage(message)
                            } else {
                                message.id = modifyCI.message.id
                                db.updateMessage(message)
                            }
                            if (!isModify && !isInsert) {
                                adapter.add(ChatRightText(message, s))
                            } else if (isInsert) {
                                adapter.insertMessage(ChatRightText(message, s))
                            } else {
                                adapter.updateMessage(ChatRightText(message, s))
                            }
                            chat.news = s
                            db.updateChat(chat)
                        } else {
                            val message = Message(
                                0,
                                chat.id,
                                selectNikke.id,
                                selectType,
                                s,
                                if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                            )
                            if (!isModify) {
                                message.id = db.insertMessage(message)
                            } else {
                                message.id = modifyCI.message.id
                                db.updateMessage(message)
                            }
                            if (!isModify && !isInsert) {
                                adapter.add(ChatLeftText(message, s, selectNikke))
                            } else if (isInsert) {
                                adapter.insertMessage(ChatLeftText(message, s, selectNikke))
                            } else {
                                adapter.updateMessage(ChatLeftText(message, s, selectNikke))
                            }
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
                            if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                        )
                        message.id = db.insertMessage(message)
                        if (!isModify && !isInsert) {
                            adapter.add(object : ChatItem(message, s) {
                                override fun viewType(): Int = MyType.CHAT_NARRATION
                            })
                        } else if (isInsert) {
                            adapter.insertMessage(object : ChatItem(message, s) {
                                override fun viewType(): Int = MyType.CHAT_NARRATION
                            })
                        } else {
                            adapter.updateMessage(object : ChatItem(message, s) {
                                override fun viewType(): Int = MyType.CHAT_NARRATION
                            })
                        }

                        view.inputRcl.transitionToState(R.id.tureEnd)
                    }
                    3 -> {
                        val message = Message(
                            0,
                            chat.id,
                            selectNikke.id,
                            selectType,
                            s,
                            if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else {
                                modifyPosition + 1
                            }
                        )
                        if (!isModify) {
                            message.id = db.insertMessage(message)
                        } else {
                            message.id = modifyCI.message.id
                            db.updateMessage(message)
                        }
                        if (!isModify && !isInsert) {
                            adapter.add(object : ChatItem(message, s) {
                                override fun viewType(): Int = MyType.CHAT_SPLIT_LINE
                            })
                        } else if (isInsert) {
                            adapter.insertMessage(object : ChatItem(message, s) {
                                override fun viewType(): Int = MyType.CHAT_SPLIT_LINE
                            })
                        } else {
                            adapter.updateMessage(object : ChatItem(message, s) {
                                override fun viewType(): Int = MyType.CHAT_SPLIT_LINE
                            })
                        }
                        view.inputRcl.transitionToState(R.id.tureEnd)
                    }
                }
                enen()
                view.chatRv.smoothScrollToPosition(if (!isModify && !isInsert) adapter.itemCount - 1 else modifyPosition + 1)
                view.sendEt.setText("")
                hideKeyboard(view.sendEt)
                hideEmoji()
                isModify = false
                isInsert = false
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
        view.topView.singleClick {
            isModify = false
            isInsert = false
        }
    }
    private val selectNikkeListener by lazy {
        object : SelectNikkeListener {
            override fun select(nikke: Nikke) {
                hideEmoji()
                view.avatarIv.load(nikke.avatarPath, true)
                view.selectNikkeCL.gone()
                selectNikke = nikke
            }

            override fun unSelect(nikke: Nikke) {
                this.select(nikke)
            }
        }
    }
    private val modifyDialogListener: ClickModifyDialogListener = object : ClickModifyDialogListener {
        override fun onInsert(entry: ChatItem, position: Int) {
            "正常输入一条消息后插入到下面,点击顶部黄色区域取消".toast()
            modifyCI = entry
            modifyPosition = entry.message.postion
            isInsert = true
            isModify = false
        }

        override fun onUpdate(entry: ChatItem, position: Int) {
            "正常输入一条消息后覆盖此消息,点击顶部黄色区域取消".toast()
            modifyCI = entry
            modifyPosition = entry.message.postion
            modifyPosition = entry.message.postion
            isInsert = false
            isModify = true
        }

        override fun onDelete(entry: ChatItem, position: Int) {
            db.deleteMessage(entry.message)
            adapter.deleteMessage(entry.message)
        }
    }
    private val adapter: ChatAdapter = ChatAdapter(db, supportFragmentManager, modifyDialogListener)
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
                            this@ModifyChatActivity.hideEmoji()
                            AddNikkeDialog(object : AddNikkeListener {
                                override fun onClick(nikkes: ArrayList<Nikke>) {
                                    val adapter = bindingAdapter as MultipleTypeAdapter
                                    adapter.insert(adapter.itemCount - 1, nikkes)
                                    chat.nikkes?.addAll(nikkes)
                                    chat.nikkeIds?.addAll(nikkes.map { it.id })
                                    db.updateChat(chat)
                                }
                            }).show(supportFragmentManager)
                        }
                    }
                }
                else -> null
            }
        }
    }
    private val emojiAdapter: MultipleTypeAdapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.ADD_EMOJI -> {
                    object : MultipleViewHolder<ItemEmojiBinding, MyType>(ItemEmojiBinding.inflate(layoutInflater, viewGroup, false)) {
                        override fun setHolder(entity: MyType) {
                            val width = getWidth() / 5
                            view.root.layoutParams.width = width
                            view.root.layoutParams.height = width
                            view.emojiIv.load(entity.data, 6)
                            view.emojiIv.singleClick {
                                PictureSelector.create(this@ModifyChatActivity)
                                    .openGallery(SelectMimeType.TYPE_IMAGE)
                                    .setImageEngine(GlideEngine())
                                    .setMaxSelectNum(9)
                                    .isGif(true)
                                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                                        override fun onResult(result: java.util.ArrayList<LocalMedia>?) {
                                            result?.let {
                                                emojiAdapter.add(
                                                    it.map {
                                                        object : MyType(it.realPath) {
                                                            override fun viewType(): Int = MyType.EMOJI
                                                        }
                                                    }
                                                )
                                                if (selectNikke.enoji == null) {
                                                    "aa".toast()
                                                    selectNikke.enoji = ArrayList()
                                                }
                                                selectNikke.enoji!!.addAll(
                                                    it.map { it.realPath }
                                                )
                                                Log.d(TAG, "onResult: " + selectNikke.enoji)
                                                db.addEmojiByNikkeId(selectNikke.id, it)
                                            }
                                        }

                                        override fun onCancel() {
                                        }
                                    })
                            }
                        }
                    }
                }
                MyType.EMOJI -> {
                    EmojiViewHolder(ItemEmojiBinding.inflate(layoutInflater, viewGroup, false), onClickEmoji, onLongEmoji)
                }
                else -> null
            }
        }
    }
    override fun subscribeUi() {
    }
    override fun bindLayout(): ActivityChatModifyBinding = ActivityChatModifyBinding.inflate(layoutInflater)
    private val gson: Gson by lazy {
        GsonBuilder().create()
    }
    fun hideEmoji() {
        emojiAdapter.clear()
        view.emojiRv.gone()
    }
    private val onClickEmoji by lazy {
        object : ClickItemListener<String> {
            override fun onClick(data: String) {
                if ("指挥官".equals(selectNikke.name)) {
                    val message = Message(
                        0,
                        chat.id,
                        0,
                        4,
                        data,
                        if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                    )
                    if (!isModify) {
                        message.id = db.insertMessage(message)
                    } else {
                        message.id = modifyCI.message.id
                        Log.d("addMessages", "onResult: " + message.postion + message.postion)
                        db.updateMessage(message)
                    }
                    if (!isModify && !isInsert) {
                        adapter.add(ChatRightImage(message, data))
                    } else if (isInsert) {
                        adapter.insertMessage(ChatRightImage(message, data))
                    } else {
                        adapter.updateMessage(ChatRightImage(message, data))
                    }
                } else {
                    val message = Message(
                        0,
                        chat.id,
                        selectNikke.id,
                        4,
                        data,
                        if (!isModify && !isInsert) adapter.itemCount else if (isModify) modifyPosition else modifyPosition + 1
                    )
                    if (!isModify) {
                        message.id = db.insertMessage(message)
                    } else {
                        message.id = modifyCI.message.id
                        db.updateMessage(message)
                    }
                    if (!isModify && !isInsert) {
                        adapter.add(ChatLeftImage(message, data, selectNikke))
                    } else if (isInsert) {
                        adapter.insertMessage(ChatLeftImage(message, data, selectNikke))
                    } else {
                        adapter.updateMessage(ChatLeftImage(message, data, selectNikke))
                    }
                }
                this@ModifyChatActivity.view.chatRv.smoothScrollToPosition(if (!isModify && !isInsert) adapter.itemCount else modifyPosition + 1)
                this@ModifyChatActivity.hideEmoji()
                isModify = false
                isInsert = false
            }
            override fun onClick(data: String, position: Int) {
                onClick(data)
            }
        }
    }
    private val onLongEmoji by lazy {
        object : LongItemListener {
            override fun onLongClick(location: IntArray, position: Int, entry: MyType) {
                val dialog = PreviewEmojiDialog(
                    location,
                    entry,
                    object : ClickPreviewEmojiDialogListener {
                        override fun onClockMoveTop(entry: MyType) {
                            emojiAdapter.moveToPostion(entry, 1)
                            selectNikke.enoji?.let {
                                it.remove(entry.data)
                                it.add(0, entry.data)
                                db.updateEmojiByNikke(selectNikke)
                                enen()
                            }
                        }

                        override fun onClockDelete(entry: MyType) {
                            emojiAdapter.remove(entry)
                            selectNikke.enoji?.remove(entry.data)
                            db.updateEmojiByNikke(selectNikke)
                            Log.d(TAG, "onClockDelete: " + selectNikke.enoji)
                            enen()
                        }

                        override fun onClock(entry: MyType) {
                            onClickEmoji.onClick(entry.data)
                        }
                    }
                )
                dialog.show(supportFragmentManager, dialog::class.simpleName)
                enen()
            }
        }
    }
}